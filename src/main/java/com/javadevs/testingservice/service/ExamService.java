package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.command.create.CreateExamCommand;
import com.javadevs.testingservice.repository.ExamRepository;
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

    @Transactional
    public Exam saveExam(CreateExamCommand command) {
        Student student = studentService.findStudentById(command.getStudentId());
        Set<Question> questions = questionService.findQuestionsByIds(command.getQuestions());

        Exam exam = Exam.builder()
                .createdAt(LocalDate.now())
                .student(student)
                .questions(questions)
                .description(command.getDescription())
                .build();

        student.assignExam(exam);

        return examRepository.save(exam);
    }

    @Transactional(readOnly = true)
    public Exam findExamById(long id) {
        return examRepository.findExamById(id)
                .orElseThrow(() -> new RuntimeException((String.format("Exam with id %s not found!", id))));
    }

    @Transactional(readOnly = true)
    public Page<Exam> findAllExams(Pageable pageable) {
        return examRepository.findAll(pageable);
    }
}
