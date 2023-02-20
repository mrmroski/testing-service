package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.DatabaseCleaner;
import com.javadevs.testingservice.TestingServiceApplication;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.DeleteSubjectCoveredFromStudentCommand;
import com.javadevs.testingservice.repository.StudentRepository;
import com.javadevs.testingservice.repository.SubjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestingServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc postman;

//    @AfterEach
//    void tearDown() throws LiquibaseException {
//        databaseCleaner.cleanUp();
//    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    @Test
    void itShouldSaveStudent() throws Exception {
        CreateStudentCommand command = new CreateStudentCommand("Robert", "Lewandowski", "rl@02.pl");
        String commandString = mapper.writeValueAsString(command);

        postman.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Robert"))
                .andExpect(jsonPath("$.lastname").value("Lewandowski"))
                .andExpect(jsonPath("$.email").value("rl@02.pl"))
                .andExpect(jsonPath("$.startedAt").value(LocalDate.now().toString()));
    }

    @Test
    void itShouldFindStudentById() throws Exception {
        Student student = new Student();
        student.setName("Robert");
        student.setLastname("Lewandowski");
        student.setEmail("rl@02.pl");
        student.setStartedAt(LocalDate.now());
        student.setExams(new HashSet<>());
        student.setSubjectsCovered(new HashSet<>());

        Student savedStudent = studentRepository.save(student);

        postman.perform(get("/api/v1/students/" + savedStudent.getId()))
                .andExpect(jsonPath("$.id").value(savedStudent.getId()))
                .andExpect(jsonPath("$.name").value("Robert"))
                .andExpect(jsonPath("$.lastname").value("Lewandowski"))
                .andExpect(jsonPath("$.email").value("rl@02.pl"))
                .andExpect(jsonPath("$.startedAt").value(LocalDate.now().toString()));
    }

    @Test
    void itShouldFindAllStudents() throws Exception {
        Student student = new Student();
        student.setName("Robert");
        student.setLastname("Lewandowski");
        student.setEmail("rl@02.pl");
        student.setStartedAt(LocalDate.now());
        student.setExams(new HashSet<>());
        student.setSubjectsCovered(new HashSet<>());

        Student savedStudent = studentRepository.save(student);

        postman.perform(get("/api/v1/students"))
                .andExpect(jsonPath("$.content[0].id").value(savedStudent.getId()))
                .andExpect(jsonPath("$.content[0].name").value("Robert"))
                .andExpect(jsonPath("$.content[0].lastname").value("Lewandowski"))
                .andExpect(jsonPath("$.content[0].email").value("rl@02.pl"))
                .andExpect(jsonPath("$.content[0].startedAt").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void itShouldDeleteStudent() throws Exception {
        Student student = new Student();
        student.setName("Robert");
        student.setLastname("Lewandowski");
        student.setEmail("rl@02.pl");
        student.setStartedAt(LocalDate.now());
        student.setExams(new HashSet<>());
        student.setSubjectsCovered(new HashSet<>());

        Student savedStudent = studentRepository.save(student);

        postman.perform(delete("/api/v1/students/" + savedStudent.getId()))
                .andExpect(status().isNoContent());

        postman.perform(get("/api/v1/students/" + savedStudent.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void itShouldEditStudentPartially() throws Exception {
        Student student = new Student();
        student.setName("Robert");
        student.setLastname("Lewandowski");
        student.setEmail("rl@02.pl");
        student.setStartedAt(LocalDate.now());
        student.setExams(new HashSet<>());
        student.setSubjectsCovered(new HashSet<>());

        Student savedStudent = studentRepository.save(student);

        EditStudentCommand command = EditStudentCommand.builder()
                .lastname("Blaszczykowski").build();

        String commandString = mapper.writeValueAsString(command);

        postman.perform(put("/api/v1/students/" + savedStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandString))
                .andExpect(status().isOk());

        postman.perform(get("/api/v1/students/" + savedStudent.getId()))
                .andExpect(jsonPath("$.name").value("Robert"))
                .andExpect(jsonPath("$.lastname").value("Blaszczykowski"));
    }

    @Test
    void itShouldAddSubjectToStudent() throws Exception {

        Student student = new Student();
        student.setName("Robert");
        student.setLastname("Lewandowski");
        student.setEmail("rl@02.pl");
        student.setStartedAt(LocalDate.now());
        student.setExams(new HashSet<>());
        student.setSubjectsCovered(new HashSet<>());

        Student savedStudent = studentRepository.save(student);

        Subject subject = new Subject();
        subject.setSubject("Petle");
        subject.setDescription("Wprowadzenie do petli");
        subject.setQuestions(new HashSet<>());

        Subject savedSubject = subjectRepository.save(subject);

        AddSubjectCoveredToStudentCommand command = AddSubjectCoveredToStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(savedSubject.getId())
                .build();

        String commandString = mapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/students/" + savedStudent.getId() + "/addSubject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandString))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldDeleteSubjectFromStudent() throws Exception {

        Student student = new Student();
        student.setName("Robert");
        student.setLastname("Lewandowski");
        student.setEmail("rl@02.pl");
        student.setStartedAt(LocalDate.now());
        student.setExams(new HashSet<>());
        student.setSubjectsCovered(new HashSet<>());

        Student savedStudent = studentRepository.save(student);

        Subject subject = new Subject();
        subject.setSubject("Petle");
        subject.setDescription("Wprowadzenie do petli");
        subject.setQuestions(new HashSet<>());

        Subject savedSubject = subjectRepository.save(subject);

        AddSubjectCoveredToStudentCommand command = AddSubjectCoveredToStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(savedSubject.getId())
                .build();

        String commandString = mapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/students/" + savedStudent.getId() + "/addSubject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandString))
                .andExpect(status().isOk());

        DeleteSubjectCoveredFromStudentCommand command2 = DeleteSubjectCoveredFromStudentCommand.builder()
                .studentId(savedStudent.getId())
                .subjectId(savedSubject.getId())
                .build();

        String commandString2 = mapper.writeValueAsString(command2);

        postman.perform(patch("/api/v1/students/" + savedStudent.getId() + "/deleteSubject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandString2))
                .andExpect(status().isOk());

        postman.perform(get("/api/v1/students/" + savedStudent.getId()))
                .andExpect(jsonPath("$.name").value("Robert"))
                .andExpect(jsonPath("$.lastname").value("Lewandowski"))
                .andExpect(jsonPath("$.subjects").isEmpty());
    }
}