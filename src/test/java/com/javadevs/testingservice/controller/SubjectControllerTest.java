package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.model.command.edit.EditSubjectCommand;
import com.javadevs.testingservice.repository.SubjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;

@SpringBootTest
@AutoConfigureMockMvc
public class SubjectControllerTest {
    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SubjectRepository subjectRepository;

    @BeforeEach
    void clean() {
        subjectRepository.deleteAll();
    }

    @Test
    void shouldAddSubject() throws Exception {
        //given
        CreateSubjectCommand cmd = CreateSubjectCommand.builder()
                .subject("test")
                .description("test 1")
                .build();

        String cmdRequest = mapper.writeValueAsString(cmd);

        //when
        String response = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cmdRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("test 1"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject = mapper.readValue(response, Subject.class);

        //then
        Subject saved = subjectRepository.findSubjectById(subject.getId()).get();
        Assertions.assertEquals(saved.getSubject(), "test");
        Assertions.assertEquals(saved.getDescription(), "test 1");
        Assertions.assertEquals(saved.getId(), subject.getId());
    }

    @Test
    void shouldGetAllSubjects() throws Exception {
        //given
        CreateSubjectCommand cmd = CreateSubjectCommand.builder()
                .subject("test")
                .description("test 1")
                .build();

        String cmdRequest = mapper.writeValueAsString(cmd);

        CreateSubjectCommand cmd2 = CreateSubjectCommand.builder()
                .subject("test2")
                .description("test 2")
                .build();

        String cmdRequest2 = mapper.writeValueAsString(cmd2);

        postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cmdRequest));
        postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cmdRequest2));

        //when //then

        postman.perform(MockMvcRequestBuilders.get("/api/v1/subjects"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subject").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("test 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].subject").value("test2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].description").value("test 2"));

    }

    @Test
    void shouldGetSubjectById() throws Exception {
        //given
        CreateSubjectCommand cmd = CreateSubjectCommand.builder()
                .subject("test")
                .description("test 1")
                .build();

        String cmdRequest = mapper.writeValueAsString(cmd);
        String postResp = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cmdRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject sub = mapper.readValue(postResp, Subject.class);
        //when
        String response = postman.perform(MockMvcRequestBuilders.get("/api/v1/subjects/" + sub.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sub.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("test 1"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject = mapper.readValue(response, Subject.class);

        //then
        Subject saved = subjectRepository.findSubjectById(subject.getId()).get();

        Assertions.assertEquals(saved.getId(), sub.getId());
        Assertions.assertEquals(saved.getSubject(), "test");
        Assertions.assertEquals(saved.getDescription(), "test 1");
    }

    @Test
    void shouldDeleteSubject() throws Exception {
        //given
        CreateSubjectCommand cmd = CreateSubjectCommand.builder()
                .subject("test")
                .description("test 1")
                .build();

        String cmdRequest = mapper.writeValueAsString(cmd);
        String postResp = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cmdRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject sub = mapper.readValue(postResp, Subject.class);

        //when

        postman.perform(MockMvcRequestBuilders.delete("/api/v1/subjects/" + sub.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //then
        Assertions.assertThrows(NoSuchElementException.class, () -> subjectRepository.findSubjectById(sub.getId()).get());
    }

    @Test
    void shouldEditSubject() throws Exception {
        //given
        CreateSubjectCommand cmd = CreateSubjectCommand.builder()
                .subject("test")
                .description("test 1")
                .build();

        String cmdRequest = mapper.writeValueAsString(cmd);
        String postResp = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cmdRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject sub = mapper.readValue(postResp, Subject.class);

        EditSubjectCommand editcmd = EditSubjectCommand.builder()
                .subject("t1")
                .description("t 1")
                .build();
        String editSub = mapper.writeValueAsString(editcmd);

        //when
        String response = postman.perform(MockMvcRequestBuilders.put("/api/v1/subjects/" + sub.getId())
                        .content(editSub)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sub.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject").value("t1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("t 1"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject = mapper.readValue(response, Subject.class);

        //then
        Assertions.assertEquals(subject.getId(), sub.getId());
        Assertions.assertEquals(subject.getSubject(), "t1");
        Assertions.assertEquals(subject.getDescription(), "t 1");
    }

    @Test
    void shouldEditSubjectPartially() throws Exception {
        //given
        CreateSubjectCommand cmd = CreateSubjectCommand.builder()
                .subject("test")
                .description("test 1")
                .build();

        String cmdRequest = mapper.writeValueAsString(cmd);
        String postResp = postman.perform(MockMvcRequestBuilders.post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cmdRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject sub = mapper.readValue(postResp, Subject.class);

        EditSubjectCommand editcmd = EditSubjectCommand.builder()
                .subject("t1")
                .build();
        String editSub = mapper.writeValueAsString(editcmd);

        //when
        String response = postman.perform(MockMvcRequestBuilders.patch("/api/v1/subjects/" + sub.getId())
                        .content(editSub)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sub.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject").value("t1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("test 1"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Subject subject = mapper.readValue(response, Subject.class);

        //then
        Assertions.assertEquals(subject.getId(), sub.getId());
        Assertions.assertEquals(subject.getSubject(), "t1");
        Assertions.assertEquals(subject.getDescription(), "test 1");
    }
}
