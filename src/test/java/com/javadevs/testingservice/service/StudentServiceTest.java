package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.StudentNotFoundException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.DeleteSubjectCoveredFromStudentCommand;
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

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-tests")
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @BeforeEach
    void clean() {
        studentRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    @Test
    void shouldSaveStudentOnSaveStudent() {
        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        assertEquals("Maciej", savedStudent.getName());
        assertEquals("Zyga", savedStudent.getLastname());
        assertEquals("zyga@gmail.com", savedStudent.getEmail());
    }

    @Test
    void shouldFindStudentByIdOnFindStudentById() {
        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        Student foundedStudent = studentService.findStudentById(savedStudent.getId());

        assertEquals(savedStudent.getId(), foundedStudent.getId());
        assertEquals(savedStudent.getVersion(), foundedStudent.getVersion());
        assertEquals(savedStudent.getName(), foundedStudent.getName());
        assertEquals(savedStudent.getLastname(), foundedStudent.getLastname());
        assertEquals(savedStudent.getEmail(), foundedStudent.getEmail());
    }

    @Test
    void shouldFindAllStudentsOnFindAllStudents() {
        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        CreateStudentCommand command2 = CreateStudentCommand.builder()
                .name("Anna")
                .lastname("Piaskarka")
                .email("piaskara@gmail.com").build();

        Student savedStudent2 = studentService.saveStudent(command2);

        Page<Student> studentPage = studentService.findAllStudents(Pageable.unpaged());

        List<Student> students = studentPage.getContent();

        assertEquals(2, students.size());
        assertTrue(students.stream().anyMatch(s -> Objects.equals(s.getId(), savedStudent.getId())));
        assertTrue(students.stream().anyMatch(s -> Objects.equals(s.getId(), savedStudent2.getId())));
    }

    @Test
    void shouldDeleteStudentOnDeleteStudent() {
        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        CreateStudentCommand command2 = CreateStudentCommand.builder()
                .name("Anna")
                .lastname("Piaskarka")
                .email("piaskara@gmail.com").build();

        Student savedStudent2 = studentService.saveStudent(command2);

        studentService.deleteStudent(savedStudent.getId());

        Page<Student> studentPage = studentService.findAllStudents(Pageable.unpaged());

        List<Student> students = studentPage.getContent();

        assertEquals(1, students.size());
        assertTrue(students.stream().noneMatch(s -> Objects.equals(s.getId(), savedStudent.getId())));
        assertTrue(students.stream().anyMatch(s -> Objects.equals(s.getId(), savedStudent2.getId())));
    }

    @Test
    void shouldThrowStudentNotFoundExceptionOnDeleteStudentWhenTryingToDeleteStudentWithNonExistingId() {
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(312321321321L));
    }

    @Test
    void shouldEditStudentPartiallyOnEditStudentPartially() {
        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        EditStudentCommand editStudentCommand = EditStudentCommand.builder()
                .name("Aisha")
                .lastname("Aisha")
                .version(savedStudent.getVersion())
                .email("aisha@op.pl").build();

        Student editedStudent = studentService.editStudentPartially(savedStudent.getId(), editStudentCommand);

        assertEquals("Aisha", editedStudent.getName());
        assertEquals("Aisha", editedStudent.getLastname());
        assertEquals("aisha@op.pl", editedStudent.getEmail());
        assertEquals(savedStudent.getId(), editedStudent.getId());
        assertNotEquals(savedStudent.getVersion(), editedStudent.getVersion());
    }

    @Test
    void shouldThrowStudentNotFoundExceptionOnEditStudentPartiallyWhenTryingToEditStudentWithNonExistingId() {
        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        EditStudentCommand editStudentCommand = EditStudentCommand.builder()
                .name("Aisha")
                .lastname("Aisha")
                .version(savedStudent.getVersion())
                .email("aisha@op.pl").build();

        assertThrows(StudentNotFoundException.class,
                () -> studentService.editStudentPartially(321321312L, editStudentCommand));
    }

    @Test
    void shouldAddSubjectCoveredOnAddSubjectCovered() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        AddSubjectCoveredToStudentCommand coveredToStudentCommand = AddSubjectCoveredToStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(subject.getId()).build();

        Student studentWithAddedSubject = studentService.addSubjectCovered(coveredToStudentCommand);

        assertEquals(savedStudent.getId(), studentWithAddedSubject.getId());
        assertEquals(savedStudent.getName(), studentWithAddedSubject.getName());
        assertEquals(savedStudent.getLastname(), studentWithAddedSubject.getLastname());
        assertEquals(savedStudent.getEmail(), studentWithAddedSubject.getEmail());
        assertEquals(1, studentWithAddedSubject.getSubjects().size());
        assertTrue(studentWithAddedSubject.getSubjects().stream().anyMatch(s -> s.getId() == subject.getId()));
    }

    @Test
    void shouldThrowStudentNotFoundExceptionOnAddSubjectCoveredWhenTryingToAddSubjectToStudentWithNonExistingId() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        AddSubjectCoveredToStudentCommand coveredToStudentCommand = AddSubjectCoveredToStudentCommand.builder()
                .studentId(3423432543L)
                .subjectId(subject.getId()).build();

        assertThrows(StudentNotFoundException.class, () -> studentService.addSubjectCovered(coveredToStudentCommand));
    }

    @Test
    void shouldThrowSubjectNotFoundExceptionOnAddSubjectCoveredWhenTryingToAddSubjectWithNonExistingIdToStudent() {
        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        AddSubjectCoveredToStudentCommand coveredToStudentCommand = AddSubjectCoveredToStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(342432534L).build();

        assertThrows(SubjectNotFoundException.class, () -> studentService.addSubjectCovered(coveredToStudentCommand));
    }

    @Test
    void shouldDeleteSubjectCoveredOnDeleteSubjectCovered(){
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        AddSubjectCoveredToStudentCommand coveredToStudentCommand = AddSubjectCoveredToStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(subject.getId()).build();

        Student studentWithAddedSubject = studentService.addSubjectCovered(coveredToStudentCommand);

        DeleteSubjectCoveredFromStudentCommand coveredFromStudentCommand = DeleteSubjectCoveredFromStudentCommand.builder()
                .studentId(studentWithAddedSubject.getId())
                .subjectId(subject.getId()).build();

        Student studentAfterDeletingSubject = studentService.deleteSubjectCovered(coveredFromStudentCommand);

        assertEquals(0,studentAfterDeletingSubject.getSubjects().size());
        assertEquals("Maciej",studentAfterDeletingSubject.getName());
        assertEquals("Zyga",studentAfterDeletingSubject.getLastname());
        assertEquals("zyga@gmail.com",studentAfterDeletingSubject.getEmail());
        assertEquals(savedStudent.getId(),studentAfterDeletingSubject.getId());
    }

    @Test
    void shouldThrowStudentNotFoundExceptionOnDeleteSubjectCoveredWhenTryingToDeleteSubjectFromStudentWithNonExistingId(){
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        DeleteSubjectCoveredFromStudentCommand coveredFromStudentCommand = DeleteSubjectCoveredFromStudentCommand.builder()
                .studentId(43253454354L)
                .subjectId(subject.getId()).build();

        assertThrows(StudentNotFoundException.class,()->studentService.deleteSubjectCovered(coveredFromStudentCommand));
    }

    @Test
    void shouldThrowSubjectNotFoundExceptionOnDeleteSubjectCoveredWhenTryingToDeleteSubjectWithNonExistingIdFromStudent(){
        CreateStudentCommand command = CreateStudentCommand.builder()
                .name("Maciej")
                .lastname("Zyga")
                .email("zyga@gmail.com").build();

        Student savedStudent = studentService.saveStudent(command);

        DeleteSubjectCoveredFromStudentCommand coveredFromStudentCommand = DeleteSubjectCoveredFromStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(43243254334L).build();

        assertThrows(SubjectNotFoundException.class,()->studentService.deleteSubjectCovered(coveredFromStudentCommand));
    }
}