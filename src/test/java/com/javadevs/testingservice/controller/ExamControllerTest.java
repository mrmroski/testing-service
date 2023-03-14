package com.javadevs.testingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadevs.testingservice.repository.ExamRepository;
import com.javadevs.testingservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-tests")
class ExamControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private StudentRepository studentRepository;


    @BeforeEach
    void clean() {
        examRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void shouldSaveExam() throws Exception {
//        CreateSubjectCommand csc = CreateSubjectCommand.builder()
//                .subject("ojtaktak")
//                .description("ojoojoj")
//                .build();
//
//        String cscRequest = mapper.writeValueAsString(csc);
//
//        String cscResponse = postman.perform(post("/api/v1/subjects")
//                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
//                        .contentType(APPLICATION_JSON)
//                        .content(cscRequest))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Subject sub = mapper.readValue(cscResponse, Subject.class);
//
//        Set<CreateAnswerCommand> createAnswerCommands = Set.of(
//                CreateAnswerCommand.builder().answer("tak").correct(false).build(),
//                CreateAnswerCommand.builder().answer("nie").correct(false).build(),
//                CreateAnswerCommand.builder().answer("nie wiem").correct(true).build()
//        );
//
//        CreateQuestionClosedCommand cqc1 = CreateQuestionClosedCommand.builder()
//                .question("kafaj?")
//                .subjectId(sub.getId())
//                .answers(createAnswerCommands).build();
//
//        CreateQuestionClosedCommand cqc2 = CreateQuestionClosedCommand.builder()
//                .question("japajos?")
//                .subjectId(sub.getId())
//                .answers(createAnswerCommands).build();
//
//        String cqc1Request = mapper.writeValueAsString(cqc1);
//        String cqc2Request = mapper.writeValueAsString(cqc2);
//
//        String cqc1Response = postman.perform(post("/api/v1/questions/closed")
//                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
//                        .contentType(APPLICATION_JSON)
//                        .content(cqc1Request))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        String cqc2Response = postman.perform(post("/api/v1/questions/closed")
//                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
//                        .contentType(APPLICATION_JSON)
//                        .content(cqc2Request))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        QuestionClosed question1 = mapper.readValue(cqc1Response, QuestionClosed.class);
//        QuestionClosed question2 = mapper.readValue(cqc2Response, QuestionClosed.class);
//
//        CreateStudentCommand cstudentc = CreateStudentCommand.builder()
//                .name("Maciej")
//                .lastname("Rozgocki")
//                .email("mrozgocki@gmail.com").build();
//
//        String cstudentcRequest = mapper.writeValueAsString(cstudentc);
//
//        String cstudentcResponse = postman.perform(post("/api/v1/students")
//                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
//                        .contentType(APPLICATION_JSON)
//                        .content(cstudentcRequest))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Student student = mapper.readValue(cstudentcResponse, Student.class);
//
//        AddSubjectCoveredToStudentCommand subjectToStudentCommand = AddSubjectCoveredToStudentCommand.builder()
//                .studentId(student.getId())
//                .subjectId(sub.getId()).build();
//
//        String subjectToStudentCommandRequest = mapper.writeValueAsString(subjectToStudentCommand);
//
//        String subjectToStudentCommandResponse = postman.perform(patch("/api/v1/students/" + student.getId() + "/addSubject")
//                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
//                        .contentType(APPLICATION_JSON)
//                        .content(subjectToStudentCommandRequest))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Student studentWithSubject = mapper.readValue(subjectToStudentCommandResponse, Student.class);
//
//        CreateExamCommand cec = CreateExamCommand.builder()
//                .description("pierwszy")
//                .questions(Set.of(question1.getId(), question2.getId()))
//                .studentId(studentWithSubject.getId()).build();
//
//        String cecRequest = mapper.writeValueAsString(cec);
//
//        String cecResponse = postman.perform(post("/api/v1/exams")
//                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4=")
//                        .contentType(APPLICATION_JSON)
//                        .content(cecRequest))
//                .andExpect(status().isCreated())
//                .andReturn().getResponse().getContentAsString();
//
//        Exam exam = mapper.readValue(cecResponse, Exam.class);
//
//        String getExam = postman.perform(get("/api/v1/exams/" + exam.getId())
//                        .header(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46YWRtaW4="))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Exam finalExam = mapper.readValue(getExam, Exam.class);
//
//        assertEquals(studentWithSubject, finalExam.getStudent());
//        assertEquals("pierwszy", finalExam.getDescription());
//        assertEquals(2, finalExam.getQuestions().size());
    }

}