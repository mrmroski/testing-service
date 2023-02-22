package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.DatabaseCleaner;
import com.javadevs.testingservice.TestingServiceApplication;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.model.command.edit.EditSubjectCommand;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestingServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SubjectControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    void shouldAddSubject() throws Exception {

        CreateSubjectCommand cmd = CreateSubjectCommand.builder()
                .subject("Spring Cloud")
                .description("Mikroserwisy w cloudzie")
                .build();

        String cmdRequest = mapper.writeValueAsString(cmd);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cmdRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.subject").value("Spring Cloud"))
                .andExpect(jsonPath("$.description").value("Mikroserwisy w cloudzie"));
    }

    @Test
    void shouldGetAllSubjects() throws Exception {

        postman.perform(MockMvcRequestBuilders.get("/api/v1/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].subject").value("Petle"))
                .andExpect(jsonPath("$.content[0].description").value("Proste petle for"))
                .andExpect(jsonPath("$.numberOfElements").value(9));

    }

    @Test
    void shouldGetSubjectById() throws Exception {

        postman.perform(MockMvcRequestBuilders.get("/api/v1/subjects/" + 9))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.subject").value("Watki"))
                .andExpect(jsonPath("$.description").value("Wielewatkosc"));
    }

    @Test
    void shouldDeleteSubject() throws Exception {

        postman.perform(MockMvcRequestBuilders.delete("/api/v1/subjects/" + 1))
                .andExpect(status().isNoContent());

        postman.perform(MockMvcRequestBuilders.get("/api/v1/subjects/" + 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldEditSubject() throws Exception {

        EditSubjectCommand editcmd = EditSubjectCommand.builder()
                .description("Petle rozszerzenie")
                .version(0L)
                .build();
        String editSub = mapper.writeValueAsString(editcmd);

        postman.perform(MockMvcRequestBuilders.put("/api/v1/subjects/" + 1)
                        .content(editSub)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subject").value("Petle"))
                .andExpect(jsonPath("$.description").value("Petle rozszerzenie"))
                .andExpect(jsonPath("$.version").value(1));

        postman.perform(MockMvcRequestBuilders.get("/api/v1/subjects/" + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subject").value("Petle"))
                .andExpect(jsonPath("$.description").value("Petle rozszerzenie"))
                .andExpect(jsonPath("$.version").value(1));
    }

}
