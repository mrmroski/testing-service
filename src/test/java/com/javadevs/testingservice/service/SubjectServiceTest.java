package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.model.command.edit.EditSubjectCommand;
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
class SubjectServiceTest {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectRepository subjectRepository;

    @BeforeEach
    void clean() {
        subjectRepository.deleteAll();
    }

    @Test
    void shouldSaveSubjectOnSaveSubject() {
        CreateSubjectCommand command = CreateSubjectCommand.builder()
                .subject("Spadochroniarstwo")
                .description("Podstawy latania na czaszy").build();

        Subject savedSubject = subjectService.saveSubject(command);

        assertEquals("Spadochroniarstwo", savedSubject.getSubject());
        assertEquals("Podstawy latania na czaszy", savedSubject.getDescription());
        assertTrue(savedSubject.getId()>0);
    }

    @Test
    void shouldFindSubjectByIdOnFindSubjectById() {
        CreateSubjectCommand command = CreateSubjectCommand.builder()
                .subject("Spadochroniarstwo")
                .description("Podstawy latania na czaszy").build();

        Subject savedSubject = subjectService.saveSubject(command);

        Subject foundSubject = subjectService.findSubjectById(savedSubject.getId());

        assertEquals(savedSubject.getSubject(), foundSubject.getSubject());
        assertEquals(savedSubject.getDescription(), foundSubject.getDescription());
        assertEquals(savedSubject.getVersion(), foundSubject.getVersion());
        assertEquals(savedSubject.getId(), foundSubject.getId());
    }

    @Test
    void shouldFindAllSubjects() {
        CreateSubjectCommand command = CreateSubjectCommand.builder()
                .subject("Spadochroniarstwo")
                .description("Podstawy latania na czaszy").build();

        Subject savedSubject = subjectService.saveSubject(command);

        CreateSubjectCommand command2 = CreateSubjectCommand.builder()
                .subject("Podstawy motocrossu")
                .description("Pozycja na motocyklu").build();

        Subject savedSubject2 = subjectService.saveSubject(command2);

        Page<Subject> subjectPage = subjectService.findAllSubjects(Pageable.unpaged());

        List<Subject> subjects = subjectPage.getContent();

        assertEquals(2, subjects.size());
        assertTrue(subjects.stream().anyMatch(s -> Objects.equals(s.getId(), savedSubject.getId())));
        assertTrue(subjects.stream().anyMatch(s -> Objects.equals(s.getId(), savedSubject2.getId())));
    }

    @Test
    void shouldDeleteSubjectOnDeleteSubject() {
        CreateSubjectCommand command = CreateSubjectCommand.builder()
                .subject("Spadochroniarstwo")
                .description("Podstawy latania na czaszy").build();

        Subject savedSubject = subjectService.saveSubject(command);

        CreateSubjectCommand command2 = CreateSubjectCommand.builder()
                .subject("Podstawy motocrossu")
                .description("Pozycja na motocyklu").build();

        Subject savedSubject2 = subjectService.saveSubject(command2);

        subjectService.deleteSubject(savedSubject.getId());

        Page<Subject> subjectPage = subjectService.findAllSubjects(Pageable.unpaged());

        List<Subject> subjects = subjectPage.getContent();

        assertEquals(1, subjects.size());
        assertTrue(subjects.stream().noneMatch(s -> Objects.equals(s.getId(), savedSubject.getId())));
        assertTrue(subjects.stream().anyMatch(s -> Objects.equals(s.getId(), savedSubject2.getId())));
    }

    @Test
    void shouldThrowSubjectNotFoundExceptionOnDeleteSubjectWhenTryingToDeleteSubjectWithNonExistingId() {
        assertThrows(SubjectNotFoundException.class, () -> subjectService.deleteSubject(32432454343L));
    }

    @Test
    void shouldEditSubjectPartiallyOnEditSubjectPartially() {
        CreateSubjectCommand command = CreateSubjectCommand.builder()
                .subject("Spadochroniarstwo")
                .description("Podstawy latania na czaszy").build();

        Subject savedSubject = subjectService.saveSubject(command);

        EditSubjectCommand subjectCommand = EditSubjectCommand.builder()
                .subject("Paralotniarstwo")
                .description("Start za wyciagarka")
                .version(savedSubject.getVersion()).build();

        Subject editedSubject = subjectService.editSubjectPartially(savedSubject.getId(), subjectCommand);

        assertEquals(savedSubject.getId(), editedSubject.getId());
        assertEquals("Paralotniarstwo", editedSubject.getSubject());
        assertEquals("Start za wyciagarka", editedSubject.getDescription());
        assertNotEquals(savedSubject.getVersion(), editedSubject.getVersion());
    }

    @Test
    void shouldThrowSubjectNotFoundExceptionOnEditSubjectPartiallyWhenTryingToEditSubjectWithNonExistingId() {
        CreateSubjectCommand command = CreateSubjectCommand.builder()
                .subject("Spadochroniarstwo")
                .description("Podstawy latania na czaszy").build();

        Subject savedSubject = subjectService.saveSubject(command);
        EditSubjectCommand subjectCommand = EditSubjectCommand.builder()
                .subject("Paralotniarstwo")
                .description("Start za wyciagarka")
                .version(savedSubject.getVersion()).build();

        assertThrows(SubjectNotFoundException.class,
                () -> subjectService.editSubjectPartially(21423432L, subjectCommand));
    }
}