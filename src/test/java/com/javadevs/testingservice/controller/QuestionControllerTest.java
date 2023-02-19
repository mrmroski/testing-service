package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.QuestionType;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateAnswerCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionCommand;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.repository.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {
    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void clean() {
        questionRepository.deleteAll();
    }

    @Test
    void shouldAddQuestion() throws Exception {
        //given
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("stolice")
                .description("stolice 1")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subjectS))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject = mapper.readValue(subResp, Subject.class);

        Set<CreateAnswerCommand> answers = Set.of(
                CreateAnswerCommand.builder().answer("waw").correct(Boolean.TRUE).build(),
                CreateAnswerCommand.builder().answer("lub").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("gda").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("krk").correct(Boolean.FALSE).build()
        );

        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
                .question("stolica polski?")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .answers(answers)
                .subjectId(subject.getId())
                .build();
        String createQString = mapper.writeValueAsString(qCmd);

        //when
        String response = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createQString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.question").value("stolica polski?"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.questionType").value("MULTIPLE_CHOICE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.answers.length()").value(4))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Question question = mapper.readValue(response, Question.class);

        //then
        Question saved = questionRepository.findById(question.getId()).get();
        Assertions.assertEquals(saved.getQuestion(), "stolica polski?");
        Assertions.assertEquals(saved.getQuestionType(), QuestionType.MULTIPLE_CHOICE);
        Assertions.assertEquals(saved.getSubject(), subject);
    }

    @Test
    void shouldGetAllQuestions() throws Exception {
        //given
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("stolice")
                .description("stolice 1")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject = mapper.readValue(subResp, Subject.class);

        Set<CreateAnswerCommand> answers = Set.of(
                CreateAnswerCommand.builder().answer("waw").correct(Boolean.TRUE).build(),
                CreateAnswerCommand.builder().answer("lub").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("gda").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("krk").correct(Boolean.FALSE).build()
        );

        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
                .question("stolica polski?")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .answers(answers)
                .subjectId(subject.getId())
                .build();
        String createQString = mapper.writeValueAsString(qCmd);

        String respQ = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createQString)
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
        Question question = mapper.readValue(respQ, Question.class);

        //when then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/questions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(question.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].question").value("stolica polski?"));
    }

    @Test
    void shouldGetQuestionById() throws Exception {
        //given
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("stolice")
                .description("stolice 1")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject = mapper.readValue(subResp, Subject.class);

        Set<CreateAnswerCommand> answers = Set.of(
                CreateAnswerCommand.builder().answer("waw").correct(Boolean.TRUE).build(),
                CreateAnswerCommand.builder().answer("lub").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("gda").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("krk").correct(Boolean.FALSE).build()
        );

        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
                .question("stolica polski?")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .answers(answers)
                .subjectId(subject.getId())
                .build();
        String createQString = mapper.writeValueAsString(qCmd);

        String respQ = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createQString)
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
        Question question = mapper.readValue(respQ, Question.class);

        //when then
        postman.perform(MockMvcRequestBuilders.get("/api/v1/questions/"+question.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(question.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.question").value("stolica polski?"));
    }

    @Test
    void shouldDeleteQuestion() throws Exception{
        //given
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("stolice")
                .description("stolice 1")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject = mapper.readValue(subResp, Subject.class);

        Set<CreateAnswerCommand> answers = Set.of(
                CreateAnswerCommand.builder().answer("waw").correct(Boolean.TRUE).build(),
                CreateAnswerCommand.builder().answer("lub").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("gda").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("krk").correct(Boolean.FALSE).build()
        );

        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
                .question("stolica polski?")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .answers(answers)
                .subjectId(subject.getId())
                .build();
        String createQString = mapper.writeValueAsString(qCmd);

        String respQ = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createQString)
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
        Question question = mapper.readValue(respQ, Question.class);

        //when
        postman.perform(MockMvcRequestBuilders.delete("/api/v1/questions/"+question.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //then
        Assertions.assertThrows(NoSuchElementException.class, () -> questionRepository.findById(question.getId()).get());
    }

    @Test
    void shoudEditQuestion() throws Exception{
        //given
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("stolice")
                .description("stolice 1")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject = mapper.readValue(subResp, Subject.class);

        Set<CreateAnswerCommand> answers = Set.of(
                CreateAnswerCommand.builder().answer("waw").correct(Boolean.TRUE).build(),
                CreateAnswerCommand.builder().answer("lub").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("gda").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("krk").correct(Boolean.FALSE).build()
        );

        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
                .question("stolica polski?")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .answers(answers)
                .subjectId(subject.getId())
                .build();
        String createQString = mapper.writeValueAsString(qCmd);

        String respQ = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createQString)
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
        Question question = mapper.readValue(respQ, Question.class);

        //when
    }
}