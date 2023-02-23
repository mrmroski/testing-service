package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.repository.ExamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ExamControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ExamRepository examRepository;

    @BeforeEach
    void clean() {
        examRepository.deleteAll();
    }

}