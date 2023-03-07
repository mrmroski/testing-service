package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateAnswerCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionCommand;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.model.command.questionEdit.AddAnswerCommand;
import com.javadevs.testingservice.model.command.questionEdit.DeleteAnswerCommand;
import com.javadevs.testingservice.repository.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
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

//        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
//                .question("stolica polski?")
//                .answers(answers)
//                .subjectId(subject.getId())
//                .build();
       // String createQString = mapper.writeValueAsString(qCmd);

        //when

//        String response = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
//                        .contentType(MediaType.APPLICATION_JSON)
//                       // .content(createQString))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.question").value("stolica polski?"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.questionType").value("MULTIPLE_CHOICE"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.subject.id").value(subject.getId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.answers.length()").value(4))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        Question question = mapper.readValue(response, Question.class);

        //then
//        Question saved = questionRepository.findById(question.getId()).get();
//        Assertions.assertEquals(saved.getQuestion(), "stolica polski?");
//        Assertions.assertEquals(saved.getSubject(), subject);

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

        CreateSubjectCommand sCmd2 = CreateSubjectCommand.builder()
                .subject("anatomia")
                .description("dupy strona")
                .build();

        String subjectS2 = mapper.writeValueAsString(sCmd2);

        String subResp2 = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subjectS2))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject2 = mapper.readValue(subResp2, Subject.class);

        Set<CreateAnswerCommand> answers = Set.of(
                CreateAnswerCommand.builder().answer("waw").correct(Boolean.TRUE).build(),
                CreateAnswerCommand.builder().answer("lub").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("gda").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("krk").correct(Boolean.FALSE).build()
        );


//        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
//                .question("stolica polski?")
//                .answers(answers)
//                .subjectId(subject.getId())
//                .build();
//        String createQString = mapper.writeValueAsString(qCmd);

//        String respQ = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(createQString)
//        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
//        Question question = mapper.readValue(respQ, Question.class);

//        //when then
//        postman.perform(MockMvcRequestBuilders.get("/api/v1/questions"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(question.getId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].question").value("stolica polski?"));
        Set<CreateAnswerCommand> answers2 = Set.of(
                CreateAnswerCommand.builder().answer("kupa").correct(Boolean.TRUE).build(),
                CreateAnswerCommand.builder().answer("dupa").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("ass").correct(Boolean.FALSE).build(),
                CreateAnswerCommand.builder().answer("anus").correct(Boolean.FALSE).build()
        );



        CreateQuestionCommand qCmd2 = CreateQuestionCommand.builder()
                .question("Dupsko po lacinie?")
                .answers(answers2)
                .subjectId(subject2.getId())
                .build();
        String createQString2 = mapper.writeValueAsString(qCmd2);

        String respQ2 = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createQString2))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Question question2 = mapper.readValue(respQ2, Question.class);


                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(question2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].question").value("Dupsko po lacinie?"));
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
//
//        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
//                .question("stolica polski?")
//                .answers(answers)
//                .subjectId(subject.getId())
//                .build();
//        String createQString = mapper.writeValueAsString(qCmd);

//        String respQ = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(createQString)
//        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
//        Question question = mapper.readValue(respQ, Question.class);
//
//        //when then
//        postman.perform(MockMvcRequestBuilders.get("/api/v1/questions/" + question.getId()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(question.getId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.question").value("stolica polski?"));
    }

    @Test
    void shouldDeleteQuestion() throws Exception {
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

//        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
//                .question("stolica polski?")
//                .answers(answers)
//                .subjectId(subject.getId())
//                .build();
//        String createQString = mapper.writeValueAsString(qCmd);

//        String respQ = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(createQString)
//        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
//        Question question = mapper.readValue(respQ, Question.class);

        //when
//        postman.perform(MockMvcRequestBuilders.delete("/api/v1/questions/" + question.getId()))
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//
//        //then
//        Assertions.assertThrows(NoSuchElementException.class, () -> questionRepository.findById(question.getId()).get());
    }

    @Test
    void shouldEditQuestion() throws Exception {
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
//
//        CreateQuestionCommand qCmd = CreateQuestionCommand.builder()
//                .question("stolica polski?")
//                .answers(answers)
//                .subjectId(subject.getId())
//                .build();
//        String createQString = mapper.writeValueAsString(qCmd);

//        String respQ = postman.perform(MockMvcRequestBuilders.post("/api/v1/questions")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(createQString)
//        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
//        Question question = mapper.readValue(respQ, Question.class);

        //when
    }

    @Test
    void shouldAddAnswer() throws Exception {
        CreateSubjectCommand csc = CreateSubjectCommand.builder()
                .subject("polski rap")
                .description("podstawowa wiedza o polskim rapie")
                .build();

        String cscRequest = mapper.writeValueAsString(csc);

        String cscResponse = postman.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cscRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Subject subject = mapper.readValue(cscResponse, Subject.class);

        Set<CreateAnswerCommand> cacSet = Set.of(
                CreateAnswerCommand.builder().answer("bo tak").correct(false).build(),
                CreateAnswerCommand.builder().answer("nie jest").correct(false).build()
        );

        CreateQuestionCommand cqc = CreateQuestionCommand.builder()
                .question("Dlaczego Tede kurwą jest")
                .answers(cacSet)
                .subjectId(subject.getId()).build();

        String cqcRequest = mapper.writeValueAsString(cqc);

        String cqcResponse = postman.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cqcRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Question question = mapper.readValue(cqcResponse, Question.class);

        AddAnswerCommand aac = AddAnswerCommand.builder()
                .questionId(question.getId())
                .answer("Bo Peja tak nawinął")
                .correct(true).build();

        String aacRequest = mapper.writeValueAsString(aac);

        postman.perform(patch("/api/v1/questions/" + question.getId() + "/addAnswer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(aacRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        postman.perform(get("/api/v1/questions/" + question.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(question.getId()))
                .andExpect(jsonPath("$.question").value("Dlaczego Tede kurwą jest"))
                .andExpect(jsonPath("$.subject.id").value(question.getSubject().getId()))
                .andExpect(jsonPath("$.answers.[*].answer", containsInAnyOrder("Bo Peja tak nawinął", "bo tak", "nie jest")));
    }

    @Test
    void shouldDeleteAnswer() throws Exception {
        CreateSubjectCommand csc = CreateSubjectCommand.builder()
                .subject("polski rap")
                .description("podstawowa wiedza o polskim rapie")
                .build();

        String cscRequest = mapper.writeValueAsString(csc);

        String cscResponse = postman.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cscRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Subject subject = mapper.readValue(cscResponse, Subject.class);

        Set<CreateAnswerCommand> cacSet = Set.of(
                CreateAnswerCommand.builder().answer("bo tak").correct(false).build(),
                CreateAnswerCommand.builder().answer("nie jest").correct(false).build(),
                CreateAnswerCommand.builder().answer("Bo Peja tak nawinal").correct(true).build()
        );

        CreateQuestionCommand cqc = CreateQuestionCommand.builder()
                .question("Dlaczego Tede kurwą jest")
                .answers(cacSet)
                .subjectId(subject.getId()).build();

        String cqcRequest = mapper.writeValueAsString(cqc);

        String cqcResponse = postman.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cqcRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Question question = mapper.readValue(cqcResponse, Question.class);

        postman.perform(get("/api/v1/questions/" + question.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(question.getId()))
                .andExpect(jsonPath("$.question").value("Dlaczego Tede kurwą jest"))
                .andExpect(jsonPath("$.subject.id").value(question.getSubject().getId()))
                .andExpect(jsonPath("$.answers.[*].answer", containsInAnyOrder("Bo Peja tak nawinal","bo tak", "nie jest")));

        Long answerToDeleteId = question.getAnswers().stream()
                .filter(answer -> answer.getAnswer().contains("Bo Peja tak nawinal"))
                .map(Answer::getId).findFirst().orElseThrow(NoSuchElementException::new);

        DeleteAnswerCommand dac = DeleteAnswerCommand.builder()
                .answerId(answerToDeleteId)
                .questionId(question.getId()).build();

        String dacRequest = mapper.writeValueAsString(dac);

        postman.perform(patch("/api/v1/questions/" + question.getId() + "/deleteAnswer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dacRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(question.getId()))
                .andExpect(jsonPath("$.question").value("Dlaczego Tede kurwą jest"))
                .andExpect(jsonPath("$.subject.id").value(question.getSubject().getId()))
                .andExpect(jsonPath("$.answers.[*].answer", containsInAnyOrder("bo tak", "nie jest")));
    }
}