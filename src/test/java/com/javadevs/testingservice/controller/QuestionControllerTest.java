package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.DatabaseCleaner;
import com.javadevs.testingservice.TestingServiceApplication;
import com.javadevs.testingservice.model.QuestionType;
import com.javadevs.testingservice.model.command.create.CreateAnswerCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestingServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuestionControllerTest {

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
    void shouldAddQuestion() throws Exception {

        Set<CreateAnswerCommand> answers = Set.of(
                CreateAnswerCommand.builder().answer("waw").correct(Boolean.TRUE).build(),
                CreateAnswerCommand.builder().answer("lub").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("gda").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("krk").correct(Boolean.FALSE).build()
        );

        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
                .question("stolica polski?")
                .answers(answers)
                .subjectId(5L)
                .build();
        String createQString = mapper.writeValueAsString(qCmd);

        postman.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createQString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.question").value("stolica polski?"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject.id").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.answers.length()").value(4));
    }

    @Test
    void shouldGetAllQuestions() throws Exception {

        postman.perform(MockMvcRequestBuilders.get("/api/v1/questions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].question").value("Question1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(9));
    }

    @Test
    void shouldGetQuestionById() throws Exception {

        postman.perform(MockMvcRequestBuilders.get("/api/v1/questions/" + 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.question").value("Question1"));
    }

    @Test
    void shouldDeleteQuestion() throws Exception {

        postman.perform(MockMvcRequestBuilders.delete("/api/v1/questions/" + 1))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        postman.perform(MockMvcRequestBuilders.get("/api/v1/questions/" + 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void shouldEditQuestion() throws Exception {

        EditQuestionCommand command = EditQuestionCommand.builder()
                .question("Poprawione pytanie")
                .version(0L)
                .build();

        String stringCommand = mapper.writeValueAsString(command);

        postman.perform(MockMvcRequestBuilders.put("/api/v1/questions/" + 1)
                        .content(stringCommand)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("Poprawione pytanie"))
                .andExpect(jsonPath("$.version").value(1));
    }
}