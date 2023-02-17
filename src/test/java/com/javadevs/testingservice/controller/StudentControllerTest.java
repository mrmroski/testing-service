package com.javadevs.testingservice.controller;

import com.javadevs.testingservice.DatabaseCleaner;
import com.javadevs.testingservice.TestingServiceApplication;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = TestingServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MockMvc postman;

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    void itShouldFindStudentById() throws Exception {

        postman.perform(get("/api/v1/students/5"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Mateusz"))
                .andExpect(jsonPath("$.lastname").value("Morawiecki"))
                .andExpect(jsonPath("$.email").value("mariusz@gov.pl"))
                .andExpect(jsonPath("$.startedAt").value(LocalDate.of(2021, 1, 8).toString()));
    }


}