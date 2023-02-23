package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.DatabaseCleaner;
import com.javadevs.testingservice.TestingServiceApplication;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.DeleteSubjectCoveredFromStudentCommand;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestingServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc postman;

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
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

        postman.perform(get("/api/v1/students/" + 5))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Mateusz"))
                .andExpect(jsonPath("$.lastname").value("Morawiecki"))
                .andExpect(jsonPath("$.email").value("mariusz@gov.pl"))
                .andExpect(jsonPath("$.startedAt").value(LocalDate.of(2021, 1, 8).toString()));
    }

    @Test
    void itShouldFindAllStudents() throws Exception {

        postman.perform(get("/api/v1/students"))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Tomek"))
                .andExpect(jsonPath("$.content[0].lastname").value("Baryla"))
                .andExpect(jsonPath("$.content[0].email").value("tomek@gmail.com"))
                .andExpect(jsonPath("$.content[0].startedAt").value(LocalDate.of(2022, 5, 4).toString()))
                .andExpect(jsonPath("$.totalElements").value(9));
    }

    @Test
    void itShouldDeleteStudent() throws Exception {

        postman.perform(delete("/api/v1/students/" + 1))
                .andExpect(status().isNoContent());

        postman.perform(get("/api/v1/students/" + 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void itShouldEditStudentPartially() throws Exception {

        EditStudentCommand command = EditStudentCommand.builder()
                .lastname("Blaszczykowski")
                .version(0L)
                .build();

        String commandString = mapper.writeValueAsString(command);

        postman.perform(put("/api/v1/students/" + 7)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandString))
                .andExpect(status().isOk());

        postman.perform(get("/api/v1/students/" + 7))
                .andExpect(jsonPath("$.name").value("Robert"))
                .andExpect(jsonPath("$.lastname").value("Blaszczykowski"))
                .andExpect(jsonPath("$.version").value(1));
    }

    @Test
    void itShouldAddSubjectToStudent() throws Exception {

        AddSubjectCoveredToStudentCommand command = AddSubjectCoveredToStudentCommand.builder()
                .studentId(2L)
                .subjectId(9L)
                .build();

        String commandString = mapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/students/" + 2 + "/addSubject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandString))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldDeleteSubjectFromStudent() throws Exception {

        DeleteSubjectCoveredFromStudentCommand command2 = DeleteSubjectCoveredFromStudentCommand.builder()
                .studentId(1L)
                .subjectId(5L)
                .build();

        String commandString2 = mapper.writeValueAsString(command2);

        postman.perform(patch("/api/v1/students/" + 1 + "/deleteSubject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandString2))
                .andExpect(status().isOk());
    }
}