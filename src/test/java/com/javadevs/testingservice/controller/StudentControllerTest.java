package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.DatabaseCleaner;
import com.javadevs.testingservice.TestingServiceApplication;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.repository.StudentRepository;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private ObjectMapper mapper;

    @Autowired
    private MockMvc postman;

//    @AfterEach
//    void tearDown() throws LiquibaseException {
//        databaseCleaner.cleanUp();
//    }

//    @Test
//    void itShouldFindStudentById() throws Exception {
//
//        postman.perform(get("/api/v1/students/5"))
//                .andExpect(jsonPath("$.id").value(5))
//                .andExpect(jsonPath("$.name").value("Mateusz"))
//                .andExpect(jsonPath("$.lastname").value("Morawiecki"))
//                .andExpect(jsonPath("$.email").value("mariusz@gov.pl"))
//                .andExpect(jsonPath("$.startedAt").value(LocalDate.of(2021, 1, 8).toString()));
//    }


    @Test
    void itShouldEditStudentPartially() throws Exception {
        Student student = new Student();
        student.setName("Tomek");
        student.setEmail("tom@o2.pl");
        student.setLastname("Walczak");
        student.setStartedAt(LocalDate.now());

        Student savedStudent = studentRepository.save(student);

        EditStudentCommand command = new EditStudentCommand();
        command.setLastname("Anrzejczyk");

        String commandString = mapper.writeValueAsString(command);

        postman.perform(MockMvcRequestBuilders.put("/api/v1/students/" + savedStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandString))
                .andExpect(status().isOk());

        postman.perform(get("/api/v1/students/" + savedStudent.getId()));

    }
}