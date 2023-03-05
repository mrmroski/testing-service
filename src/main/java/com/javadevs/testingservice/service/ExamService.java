package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.StudentSubjectsNotCoveredException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.ExamResult;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateExamCommand;
import com.javadevs.testingservice.repository.ExamRepository;
import com.javadevs.testingservice.repository.ExamResultRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final ExamResultRepository examResultRepository;
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
    public void checkTest(long examId, Map<String, String> params) {
        setEndTime(LocalDateTime.now());

        List<String> answers = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().startsWith("answer_")) {
                answers.add(entry.getValue());
            }
        }

        Exam fetchedExam = findExamById(examId);
        Set<Question> questions = fetchedExam.getQuestions();
        List<Question> questionList = new ArrayList<>(questions);
        int score = 0;

        for (int i = 0; i < questionList.size(); i++) {
            String userAnswer = answers.get(i);
            List<String> correctAnswers = questionList.get(i).getAnswers()
                    .stream()
                    .filter(Answer::getCorrect)
                    .map(Answer::getAnswer)
                    .toList();

            if (correctAnswers.contains(userAnswer)) {
                score++;
            }
        }

        saveExamResultToDB(fetchedExam, answers.size(), score);
    }

    private void saveExamResultToDB(Exam exam, int answersSize, int score) {
        long time = Duration.between(startTime, endTime).toMinutes();
        double formattedPercentageResult = getFormattedPercentageResult(score, answersSize);

        ExamResult examResult = new ExamResult();
        examResult.setPercentageResult(formattedPercentageResult);
        examResult.setTimeSpent(time);
        examResult.setStudent(exam.getStudent());

        examResultRepository.save(examResult);
        sendResult(exam.getId(), examResult.getId());

        log.info("Score is {}% with total of {} answers right and {} answers wrong and time spent of {} minutes",
                formattedPercentageResult, score, answersSize - score, time);
    }

    private double getFormattedPercentageResult(int score, int answersSize) {
        double finalPercentageScore = (double) score * 100 / answersSize;
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(finalPercentageScore).replace(",", "."));
    }

    private void sendResult(long examId, long examResultId) {
        Exam fetchedExam = findExamById(examId);
        String email = fetchedExam.getStudent().getEmail();

        emailSenderService.sendExamResult(email, examResultId);
    }
}
