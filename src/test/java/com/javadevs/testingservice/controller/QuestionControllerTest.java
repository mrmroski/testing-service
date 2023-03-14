package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.QuestionClosed;
import com.javadevs.testingservice.model.QuestionOpen;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateAnswerCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionClosedCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionOpenCommand;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
import com.javadevs.testingservice.model.command.questionEdit.AddAnswerCommand;
import com.javadevs.testingservice.model.command.questionEdit.DeleteAnswerCommand;
import com.javadevs.testingservice.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-tests")
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
    void shouldSaveQuestionClosed() throws Exception {
        //given
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("stolice")
                .description("stolice 1")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(post("/api/v1/subjects")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(status().isCreated())
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

        CreateQuestionClosedCommand questionClosedCommand = CreateQuestionClosedCommand.builder()
                .question("Stolica Polski?")
                .subjectId(subject.getId())
                .answers(answers).build();

        String questionClosedCommandReq = mapper.writeValueAsString(questionClosedCommand);

        //when

        String response = postman.perform(post("/api/v1/questions/closed")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(questionClosedCommandReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value("Stolica Polski?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answers.length()").value(4))
                .andReturn()
                .getResponse()
                .getContentAsString();

        QuestionClosed question = mapper.readValue(response, QuestionClosed.class);

        //then

        QuestionClosed saved = (QuestionClosed) questionRepository.findByIdWithAnswers(question.getId()).get();
        assertEquals(saved.getQuestion(), "Stolica Polski?");
        assertEquals(saved.getSubject().getId(), subject.getId());
        assertEquals(question.getId(), saved.getId());
        assertEquals(answers.size(), saved.getAnswers().size());
    }

    @Test
    void shouldSaveQuestionOpen() throws Exception {
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("proste pytania")
                .description("proste odpowiedzi na proste pytania")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(post("/api/v1/subjects")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Subject subject = mapper.readValue(subResp, Subject.class);

        CreateQuestionOpenCommand openCommand = CreateQuestionOpenCommand.builder()
                .question("tak czy nie ?")
                .subjectId(subject.getId())
                .answer("tak").build();

        String openCommandReq = mapper.writeValueAsString(openCommand);

        String openCommandResponse = postman.perform(post("/api/v1/questions/open")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(openCommandReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value("tak czy nie ?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answer").value("tak"))
                .andReturn().getResponse().getContentAsString();

        QuestionOpen saved = mapper.readValue(openCommandResponse, QuestionOpen.class);

        QuestionOpen found = (QuestionOpen) questionRepository.findOneWithAnswersSubject(saved.getId()).get();

        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getVersion(), found.getVersion());
        assertEquals("tak czy nie ?", found.getQuestion());
        assertEquals("tak", found.getAnswer());
        assertEquals("proste pytania", found.getSubject().getSubject());
        assertEquals("proste odpowiedzi na proste pytania", found.getSubject().getDescription());
        assertEquals(subject.getId(), found.getSubject().getId());
    }

    @Test
    void shouldGetQuestionById() throws Exception {
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("proste pytania")
                .description("proste odpowiedzi na proste pytania")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(post("/api/v1/subjects")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Subject subject = mapper.readValue(subResp, Subject.class);

        CreateQuestionOpenCommand openCommand = CreateQuestionOpenCommand.builder()
                .question("tak czy nie ?")
                .subjectId(subject.getId())
                .answer("tak").build();

        String openCommandReq = mapper.writeValueAsString(openCommand);

        String openCommandResponse = postman.perform(post("/api/v1/questions/open")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(openCommandReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value("tak czy nie ?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answer").value("tak"))
                .andReturn().getResponse().getContentAsString();

        QuestionOpen saved = mapper.readValue(openCommandResponse, QuestionOpen.class);

        postman.perform(get("/api/v1/questions/" + saved.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.question").value("tak czy nie ?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()));
    }

    @Test
    void shouldFindAllQuestions() throws Exception {
        //given
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("stolice")
                .description("stolice 1")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(post("/api/v1/subjects")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(status().isCreated())
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

        CreateQuestionClosedCommand questionClosedCommand = CreateQuestionClosedCommand.builder()
                .question("Stolica Polski?")
                .subjectId(subject.getId())
                .answers(answers).build();

        String questionClosedCommandReq = mapper.writeValueAsString(questionClosedCommand);

        //when

        String response = postman.perform(post("/api/v1/questions/closed")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(questionClosedCommandReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value("Stolica Polski?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answers.length()").value(4))
                .andReturn()
                .getResponse()
                .getContentAsString();

        QuestionClosed savedClosed = mapper.readValue(response, QuestionClosed.class);

        CreateQuestionOpenCommand openCommand = CreateQuestionOpenCommand.builder()
                .question("tak czy nie ?")
                .subjectId(subject.getId())
                .answer("tak").build();

        String openCommandReq = mapper.writeValueAsString(openCommand);

        String openCommandResponse = postman.perform(post("/api/v1/questions/open")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(openCommandReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value("tak czy nie ?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answer").value("tak"))
                .andReturn().getResponse().getContentAsString();

        QuestionOpen savedOpen = mapper.readValue(openCommandResponse, QuestionOpen.class);

        postman.perform(get("/api/v1/questions")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(savedClosed.getId()))
                .andExpect(jsonPath("$.content[0].subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.content[0].answers.size()").value(answers.size()))
                .andExpect(jsonPath("$.content[0].question").value("Stolica Polski?"))
                .andExpect(jsonPath("$.content[1].id").value(savedOpen.getId()))
                .andExpect(jsonPath("$.content[1].subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.content[1].answer").value("tak"))
                .andExpect(jsonPath("$.content[1].question").value("tak czy nie ?"));

    }

    @Test
    void shouldDeleteQuestion() throws Exception {
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("proste pytania")
                .description("proste odpowiedzi na proste pytania")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(post("/api/v1/subjects")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Subject subject = mapper.readValue(subResp, Subject.class);

        CreateQuestionOpenCommand openCommand = CreateQuestionOpenCommand.builder()
                .question("tak czy nie ?")
                .subjectId(subject.getId())
                .answer("tak").build();

        String openCommandReq = mapper.writeValueAsString(openCommand);

        String openCommandResponse = postman.perform(post("/api/v1/questions/open")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(openCommandReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value("tak czy nie ?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answer").value("tak"))
                .andReturn().getResponse().getContentAsString();

        QuestionOpen saved = mapper.readValue(openCommandResponse, QuestionOpen.class);

        postman.perform(delete("/api/v1/questions/" + saved.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4="))
                .andExpect(status().isNoContent());

        postman.perform(get("/api/v1/questions/" + saved.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4="))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("QUESTION_NOT_FOUND"))
                .andExpect(jsonPath("$.questionId").value(saved.getId()));
    }

    @Test
    void shouldEditQuestionPartially() throws Exception {
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("proste pytania")
                .description("proste odpowiedzi na proste pytania")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(post("/api/v1/subjects")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Subject subject = mapper.readValue(subResp, Subject.class);

        CreateQuestionOpenCommand openCommand = CreateQuestionOpenCommand.builder()
                .question("tak czy nie ?")
                .subjectId(subject.getId())
                .answer("tak").build();

        String openCommandReq = mapper.writeValueAsString(openCommand);

        String openCommandResponse = postman.perform(post("/api/v1/questions/open")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(openCommandReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value("tak czy nie ?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answer").value("tak"))
                .andReturn().getResponse().getContentAsString();

        QuestionOpen saved = mapper.readValue(openCommandResponse, QuestionOpen.class);

        EditQuestionCommand editQuestionCommand = EditQuestionCommand.builder()
                .question("nie czy tak?")
                .version(saved.getVersion())
                .subjectId(subject.getId()).build();

        String editQuestionReq = mapper.writeValueAsString(editQuestionCommand);

        postman.perform(patch("/api/v1/questions/" + saved.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editQuestionReq))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.question").value("nie czy tak?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answer").value("tak"));
    }

    @Test
    void shouldAddAnswer() throws Exception {
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("stolice")
                .description("stolice 1")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(post("/api/v1/subjects")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(status().isCreated())
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

        CreateQuestionClosedCommand questionClosedCommand = CreateQuestionClosedCommand.builder()
                .question("Stolica Polski?")
                .subjectId(subject.getId())
                .answers(answers).build();

        String questionClosedCommandReq = mapper.writeValueAsString(questionClosedCommand);

        String response = postman.perform(post("/api/v1/questions/closed")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(questionClosedCommandReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value("Stolica Polski?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answers.length()").value(4))
                .andReturn()
                .getResponse()
                .getContentAsString();

        QuestionClosed saved = mapper.readValue(response, QuestionClosed.class);


        AddAnswerCommand aac = AddAnswerCommand.builder()
                .questionId(saved.getId())
                .answer("poz")
                .correct(false).build();

        String aacRequest = mapper.writeValueAsString(aac);

        postman.perform(patch("/api/v1/questions/" + saved.getId() + "/addAnswer")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(aacRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.question").value(saved.getQuestion()))
                .andExpect(jsonPath("$.subject.id").value(saved.getSubject().getId()))
                .andExpect(jsonPath("$.answers.[*].answer", containsInAnyOrder("waw", "lub", "gda", "krk", "poz")));
    }

    @Test
    void shouldDeleteAnswer() throws Exception {
        CreateSubjectCommand sCmd = CreateSubjectCommand.builder()
                .subject("stolice")
                .description("stolice 1")
                .build();
        String subjectS = mapper.writeValueAsString(sCmd);

        String subResp = postman.perform(post("/api/v1/subjects")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS))
                .andExpect(status().isCreated())
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

        CreateQuestionClosedCommand questionClosedCommand = CreateQuestionClosedCommand.builder()
                .question("Stolica Polski?")
                .subjectId(subject.getId())
                .answers(answers).build();

        String questionClosedCommandReq = mapper.writeValueAsString(questionClosedCommand);

        String response = postman.perform(post("/api/v1/questions/closed")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(questionClosedCommandReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value("Stolica Polski?"))
                .andExpect(jsonPath("$.subject.id").value(subject.getId()))
                .andExpect(jsonPath("$.answers.length()").value(4))
                .andReturn()
                .getResponse()
                .getContentAsString();

        QuestionClosed saved = mapper.readValue(response, QuestionClosed.class);

        Long answerToDeleteId = saved.getAnswers().stream()
                .filter(answer -> answer.getAnswer().contains("lub"))
                .map(Answer::getId).findFirst().orElseThrow(NoSuchElementException::new);

        DeleteAnswerCommand dac = DeleteAnswerCommand.builder()
                .answerId(answerToDeleteId)
                .questionId(saved.getId()).build();

        String dacRequest = mapper.writeValueAsString(dac);

        postman.perform(patch("/api/v1/questions/" + saved.getId() + "/deleteAnswer")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dacRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.question").value("Stolica Polski?"))
                .andExpect(jsonPath("$.subject.id").value(saved.getSubject().getId()))
                .andExpect(jsonPath("$.answers.[*].answer", containsInAnyOrder("waw", "gda", "krk")));
    }
}
