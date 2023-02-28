package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.StudentNotFoundException;
import com.javadevs.testingservice.exception.StudentSubjectsNotCoveredException;
import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.QuestionExam;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.StudentSubject;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateExamCommand;
import com.javadevs.testingservice.repository.ExamRepository;
import com.javadevs.testingservice.repository.QuestionExamRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;
    private final QuestionExamRepository qeRepository;

    @Transactional
    public Exam saveExam(CreateExamCommand command) {
        Student student = studentRepository.findOneWithSubjects(command.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(command.getStudentId()));
        Set<Question> questions = questionRepository.findQuestionsByIds(command.getQuestions());

        for (Question q : questions) {
            int found = 0;
            for (StudentSubject s : student.getStudentSubjects()) {
                if (q.getSubject().getId() == s.getSubject().getId()) {
                    found += 1;
                }
            }

            if (found != questions.size()) {
                throw new StudentSubjectsNotCoveredException();
            }
        }

        Exam exam = new Exam();
        exam.setStudent(student);
        exam.setCreatedAt(LocalDate.now());
        exam.setQuestionExams(questions.stream().map(q -> new QuestionExam(exam, q)).collect(Collectors.toSet()));
        exam.setDescription(command.getDescription());
        student.assignExam(exam);

        Exam saved = examRepository.saveAndFlush(exam);
        qeRepository.saveAll(questions.stream().map(q -> new QuestionExam(exam, q)).collect(Collectors.toSet()));
        return saved;
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
}
