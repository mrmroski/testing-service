package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.StudentSubjectsNotCoveredException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateExamCommand;
import com.javadevs.testingservice.repository.ExamRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.StudentRepository;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final EmailSenderService emailSenderService;

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

        return examRepository.save(exam);
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

        emailSenderService.sendStartOfTestMail(email, questions);
    }
}
