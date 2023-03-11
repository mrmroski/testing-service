package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.StudentSubjectsNotCoveredException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final EmailSenderService emailSenderService;

    @Getter
    @Setter
    private LocalDateTime startTime;

    @Getter
    @Setter
    private LocalDateTime endTime;

    @Transactional
    public Exam saveExam(CreateExamCommand command) throws MessagingException {
        Student student = studentRepository.findOneWithSubjects(command.getStudentId())
                .orElseThrow(() -> new SubjectNotFoundException(command.getStudentId()));
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
        Set<Question> questions = fetchedExam.getQuestions();
        int score = 0;

        for (Question q : questions) {
            if (openQuestions.containsKey(q.getId())) {
                QuestionOpen opn = (QuestionOpen) q;
                String correct = opn.getAnswer();
                String given = openQuestions.get(q.getId());

                if (Objects.equals(correct, given)) {
                    score += 1;
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
                        score += 1;
                    }
                }
            }
        }

        saveExamResultToDB(fetchedExam, fetchedExam.getQuestions().size(), score);
    }

    private void saveExamResultToDB(Exam exam, int answersSize, int score) throws MessagingException {
        long time = Duration.between(startTime, endTime).toMinutes();
        double formattedPercentageResult = getFormattedPercentageResult(score, answersSize);

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
}
