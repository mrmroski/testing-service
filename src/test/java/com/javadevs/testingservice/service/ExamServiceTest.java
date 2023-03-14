package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.QuestionOpen;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateExamCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionOpenCommand;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.repository.ExamRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.StudentRepository;
import com.javadevs.testingservice.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-tests")
class ExamServiceTest {

    @Autowired
    private ExamService examService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @BeforeEach
    void clean() {
        examRepository.deleteAll();
        questionRepository.deleteAll();
        studentRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    @Test
    void shouldSaveExamOnSaveExam() throws MessagingException {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        CreateQuestionOpenCommand command2 = CreateQuestionOpenCommand.builder()
                .question("Najgorsze pieluszki?")
                .answer("Przeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);
        QuestionOpen savedQuestion2 = questionService.saveQuestionOpen(command2);

        Set<Long> questionsIds = Set.of(savedQuestion.getId(), savedQuestion2.getId());

        CreateStudentCommand createStudentCommand = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(createStudentCommand);

        AddSubjectCoveredToStudentCommand coveredToStudentCommand = AddSubjectCoveredToStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(subject.getId()).build();

        Student studentWithAddedSubject = studentService.addSubjectCovered(coveredToStudentCommand);

        CreateExamCommand examCommand = CreateExamCommand.builder()
                .studentId(studentWithAddedSubject.getId())
                .description("MUCHACHA")
                .questions(questionsIds).build();

        Exam exam = examService.saveExam(examCommand);

        assertEquals(savedStudent.getId(), exam.getStudent().getId());
        assertEquals("MUCHACHA", exam.getDescription());
        assertEquals(2, exam.getQuestions().size());
        assertTrue(exam.getQuestions().stream().anyMatch(q -> Objects.equals(q.getId(), savedQuestion.getId())));
        assertTrue(exam.getQuestions().stream().anyMatch(q -> Objects.equals(q.getId(), savedQuestion2.getId())));
        assertTrue(exam.getId() > 0);
    }

    @Test
    void shouldFindExamByIdOnFindExamById() throws MessagingException {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        CreateQuestionOpenCommand command2 = CreateQuestionOpenCommand.builder()
                .question("Najgorsze pieluszki?")
                .answer("Przeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);
        QuestionOpen savedQuestion2 = questionService.saveQuestionOpen(command2);

        Set<Long> questionsIds = Set.of(savedQuestion.getId(), savedQuestion2.getId());

        CreateStudentCommand createStudentCommand = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(createStudentCommand);

        AddSubjectCoveredToStudentCommand coveredToStudentCommand = AddSubjectCoveredToStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(subject.getId()).build();

        Student studentWithAddedSubject = studentService.addSubjectCovered(coveredToStudentCommand);

        CreateExamCommand examCommand = CreateExamCommand.builder()
                .studentId(studentWithAddedSubject.getId())
                .description("MUCHACHA")
                .questions(questionsIds).build();

        Exam exam = examService.saveExam(examCommand);

        Exam foundExam = examService.findExamById(exam.getId());

        assertEquals(exam.getId(), foundExam.getId());
        assertEquals(exam.getDescription(), foundExam.getDescription());
        assertEquals(exam.getStudent().getId(), foundExam.getStudent().getId());
        assertEquals(exam.getQuestions().size(), foundExam.getQuestions().size());
        assertTrue(foundExam.getQuestions().stream().anyMatch(q -> Objects.equals(q.getId(), savedQuestion.getId())));
        assertTrue(foundExam.getQuestions().stream().anyMatch(q -> Objects.equals(q.getId(), savedQuestion2.getId())));
    }

    @Test
    void shouldFindAllExamsOnFindAllExams() throws MessagingException {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        CreateQuestionOpenCommand command2 = CreateQuestionOpenCommand.builder()
                .question("Najgorsze pieluszki?")
                .answer("Przeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);
        QuestionOpen savedQuestion2 = questionService.saveQuestionOpen(command2);

        Set<Long> questionsIds = Set.of(savedQuestion.getId(), savedQuestion2.getId());

        CreateStudentCommand createStudentCommand = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(createStudentCommand);

        AddSubjectCoveredToStudentCommand coveredToStudentCommand = AddSubjectCoveredToStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(subject.getId()).build();

        Student studentWithAddedSubject = studentService.addSubjectCovered(coveredToStudentCommand);

        CreateExamCommand examCommand = CreateExamCommand.builder()
                .studentId(studentWithAddedSubject.getId())
                .description("MUCHACHA")
                .questions(questionsIds).build();

        Exam exam = examService.saveExam(examCommand);

        CreateStudentCommand createStudentCommand2 = CreateStudentCommand.builder()
                .name("Adam")
                .lastname("Lakuza")
                .email("lakuadam@gmail.com").build();

        Student savedStudent2 = studentService.saveStudent(createStudentCommand2);

        AddSubjectCoveredToStudentCommand coveredToStudentCommand2 = AddSubjectCoveredToStudentCommand.builder()
                .studentId(savedStudent2.getId())
                .subjectId(subject.getId()).build();

        Student studentWithAddedSubject2 = studentService.addSubjectCovered(coveredToStudentCommand2);

        CreateExamCommand examCommand2 = CreateExamCommand.builder()
                .studentId(studentWithAddedSubject2.getId())
                .description("LAKUZAAA")
                .questions(questionsIds).build();

        Exam exam2 = examService.saveExam(examCommand2);

        Page<Exam> examPage = examService.findAllExams(Pageable.unpaged());

        List<Exam> exams = examPage.getContent();

        assertEquals(2,exams.size());
        assertTrue(exams.stream().anyMatch(e -> Objects.equals(e.getId(), exam.getId())));
        assertTrue(exams.stream().anyMatch(e -> Objects.equals(e.getId(), exam2.getId())));
    }
}