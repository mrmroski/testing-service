package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.StudentSubjectsNotCoveredException;
import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateExamCommand;
import com.javadevs.testingservice.repository.ExamRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final QuestionService questionService;
    private final StudentService studentService;
    private final EmailSenderService emailSenderService;

    @Transactional
    public Exam saveExam(CreateExamCommand command) throws MessagingException {
        Student student = studentService.findStudentById(command.getStudentId());
        Set<Question> questions = questionService.findQuestionsByIds(command.getQuestions());

        for (Question q : questions) {
            boolean found = false;
            for (Subject s : student.getSubjectsCovered()) {
                if (q.getSubject().getId() == s.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new StudentSubjectsNotCoveredException();
            }
        }

        Exam exam = Exam.builder()
                .createdAt(LocalDate.now())
                .student(student)
                .questions(questions)
                .description(command.getDescription())
                .build();

        student.assignExam(exam);

        examRepository.save(exam);

        //commented because we dont want to get banned on gmail neither spam on random emails :D
        //emailSenderService.sendPreparingMail(student.getEmail(), exam.getId());

        return exam;
    }

    @Transactional(readOnly = true)
    public Exam findExamById(long id) {
        return examRepository.findExamById(id)
                .orElseThrow(() -> new ExamNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<Exam> findAllExams(Pageable pageable) {
        return examRepository.findAll(pageable);
    }

    public void sendExam(long examId) throws MessagingException {
        Exam fetchedExam = findExamById(examId);
        String email = fetchedExam.getStudent().getEmail();
        Set<Question> questions = fetchedExam.getQuestions();

        emailSenderService.sendStartOfTestMail(email, questions);
    }
}
