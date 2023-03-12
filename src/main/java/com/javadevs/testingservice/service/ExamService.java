package com.javadevs.testingservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.config.HFProperties;
import com.javadevs.testingservice.exception.ExamExpiredException;
import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.StudentNotFoundException;
import com.javadevs.testingservice.exception.StudentSubjectsNotCoveredException;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.QuestionClosed;
import com.javadevs.testingservice.model.QuestionOpen;
import com.javadevs.testingservice.model.Result;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateExamCommand;
import com.javadevs.testingservice.repository.ExamRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final EmailSenderService emailSenderService;
    private final HFProperties hfProperties;
    private final ObjectMapper objectMapper;

    @Getter
    @Setter
    private LocalDateTime startTime;

    @Getter
    @Setter
    private LocalDateTime endTime;

    @Transactional
    public Exam saveExam(CreateExamCommand command) throws MessagingException {
        Student student = studentRepository.findOneWithSubjects(command.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(command.getStudentId()));
        System.out.println(command.getQuestions());
        Set<Question> questions = questionRepository.findQuestionsByIds(command.getQuestions());

        int found = 0;
        for (Question q : questions) {
            for (Subject s : student.getSubjects()) {
                if (s.getId() == q.getSubject().getId()) {
                    found += 1;
                }
            }
        }
        if (found != questions.size()) {
            throw new StudentSubjectsNotCoveredException();
        }

        Exam exam = new Exam();
        exam.setDescription(command.getDescription());
        exam.setCreatedAt(LocalDate.now());
        exam.setStudent(student);
        exam.setQuestions(questions);
        questions.forEach(q -> q.getExams().add(exam));

        examRepository.save(exam);

        //commented because we don't want to get banned on gmail neither spam on random emails :D
        emailSenderService.sendPreparingMail(student.getEmail(), exam.getId());

        return exam;
    }

    @Transactional(readOnly = true)
    public Exam findExamById(long id) {
        return examRepository.findExamById(id)
                .orElseThrow(() -> new ExamNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<Exam> findAllExams(Pageable pageable) {
        return examRepository.findAllExams(pageable);
    }

    public void sendExam(long examId) throws MessagingException {
        Exam fetchedExam = findExamById(examId);
        String email = fetchedExam.getStudent().getEmail();
        Set<Question> questions = fetchedExam.getQuestions();

        emailSenderService.sendStartOfTestMail(email, questions, examId);
        setStartTime(LocalDateTime.now());
    }

    @Transactional
    public void checkTest(long examId, Map<String, String> params) throws MessagingException {
        setEndTime(LocalDateTime.now());

        Map<Long, String> openQuestions = new HashMap<>();
        Map<Long, List<Long>> closedQuestions = new HashMap<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().contains("-")) {
                String[] spl = entry.getKey().split("-");
                Long qid = Long.parseLong(spl[0]);
                Long aid = Long.parseLong(spl[1]);

                if (closedQuestions.containsKey(qid)) {
                    List<Long> ids = closedQuestions.getOrDefault(qid, new ArrayList<>());
                    ids.add(aid);
                    closedQuestions.put(qid, ids);
                } else {
                    List<Long> ids = new ArrayList<>();
                    ids.add(aid);
                    closedQuestions.put(qid, ids);
                }
            } else {
                Long qid = Long.parseLong(entry.getKey());
                String answer = entry.getValue();
                openQuestions.put(qid, answer);
            }
        }

        Exam fetchedExam = findExamById(examId);
        if (fetchedExam.isExpired()) {
            throw new ExamExpiredException(examId);
        }

        Set<Question> questions = fetchedExam.getQuestions();
        AtomicInteger score = new AtomicInteger();

        for (Question q : questions) {
            if (openQuestions.containsKey(q.getId())) {
                QuestionOpen opn = (QuestionOpen) q;
                String correct = opn.getAnswer();
                String given = openQuestions.get(q.getId());

                if (semanticSimilarity(correct, given)) {
                    score.addAndGet(1);
                }
            }

            if (closedQuestions.containsKey(q.getId())) {
                QuestionClosed cls = (QuestionClosed) q;
                int allCorrect = 0;
                int providedCorrect = 0;
                for (Answer a : cls.getAnswers()) {
                    if (a.getCorrect()) {
                        if (closedQuestions.get(q.getId()).contains(a.getId())) {
                            providedCorrect += 1;
                        }
                        allCorrect += 1;
                    }
                }
                if (allCorrect == providedCorrect) {
                    if (providedCorrect == closedQuestions.get(q.getId()).size()) {
                        score.addAndGet(1);
                    }
                }
            }
        }

        saveExamResultToDB(fetchedExam, fetchedExam.getQuestions().size(), score.get());
    }

    private void saveExamResultToDB(Exam exam, int answersSize, int score) throws MessagingException {
        long time = Duration.between(startTime, endTime).toMinutes();
        double formattedPercentageResult = getFormattedPercentageResult(score, answersSize);

        if (formattedPercentageResult > 50) {
            exam.setExpired(true);
        }

        Result result = Result.builder()
                .percentageResult(formattedPercentageResult)
                .timeSpent(time)
                .tryNumber(exam.getResults().size() + 1)
                .build();
        exam.getResults().add(result);
        result.setExam(exam);

        Student student = exam.getStudent();
        examRepository.save(exam);

        sendResult(student.getEmail(), exam.getId(), time);
    }

    private double getFormattedPercentageResult(int score, int answersSize) {
        double finalPercentageScore = (double) score * 100 / answersSize;
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(finalPercentageScore).replace(",", "."));
    }

    private void sendResult(String email, long examResultId, long time) throws MessagingException {
        emailSenderService.sendExamResultToStudent(email, examResultId);
        emailSenderService.sendExamResultToAdmin(email, examResultId, time);
    }

    @SneakyThrows
    private boolean semanticSimilarity(String src, String given) {
        URL url = new URL("https://api-inference.huggingface.co/models/sentence-transformers/all-mpnet-base-v2");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String body = objectMapper.writeValueAsString(new Input(src, List.of(given)));

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + hfProperties.getToken());
        conn.setRequestProperty("Content-Type", "application/json");

        conn.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes(body);
            wr.flush();
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        List<Double> similarity = objectMapper.readValue(response.toString(), new TypeReference<>(){});

        return similarity.get(0) >= 0.55;
    }
}

@Getter
@Setter
@AllArgsConstructor
class Input {
    private String source_sentence;
    private List<String> sentences;
}
