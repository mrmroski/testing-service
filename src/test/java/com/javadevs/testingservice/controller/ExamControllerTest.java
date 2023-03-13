//package com.javadevs.testingservice.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.javadevs.testingservice.model.Exam;
//import com.javadevs.testingservice.model.Question;
//import com.javadevs.testingservice.model.Student;
//import com.javadevs.testingservice.model.Subject;
//import com.javadevs.testingservice.model.command.create.CreateAnswerCommand;
//import com.javadevs.testingservice.model.command.create.CreateExamCommand;
//import com.javadevs.testingservice.model.command.create.CreateQuestionCommand;
//import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
//import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
//import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
//import com.javadevs.testingservice.repository.ExamRepository;
//import com.javadevs.testingservice.repository.StudentRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("integration-tests")
//class ExamControllerTest {
//
//    @Autowired
//    private MockMvc postman;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//    @Autowired
//    private ExamRepository examRepository;
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//
//    @BeforeEach
//    void clean() {
//        examRepository.deleteAll();
//        studentRepository.deleteAll();
//    }
//
//    @Test
//    void shouldSaveExam() throws Exception {
//
//        CreateSubjectCommand csc = CreateSubjectCommand.builder()
//                .subject("ojtaktak")
//                .description("ojoojoj")
//                .build();
//
//        String cscRequest = mapper.writeValueAsString(csc);
//
//        String cscResponse = postman.perform(post("/api/v1/subjects")
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
//        CreateQuestionCommand cqc1 = CreateQuestionCommand.builder()
//                .question("kafaj?")
//                .subjectId(sub.getId())
//                .answers(createAnswerCommands).build();
//
//        CreateQuestionCommand cqc2 = CreateQuestionCommand.builder()
//                .question("japajos?")
//                .subjectId(sub.getId())
//                .answers(createAnswerCommands).build();
//
//        String cqc1Request = mapper.writeValueAsString(cqc1);
//        String cqc2Request = mapper.writeValueAsString(cqc2);
//
//        String cqc1Response = postman.perform(post("/api/v1/questions")
//                        .contentType(APPLICATION_JSON)
//                        .content(cqc1Request))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        String cqc2Response = postman.perform(post("/api/v1/questions")
//                        .contentType(APPLICATION_JSON)
//                        .content(cqc2Request))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Question question1 = mapper.readValue(cqc1Response, Question.class);
//        Question question2 = mapper.readValue(cqc2Response, Question.class);
//
//        CreateStudentCommand cstudentc = CreateStudentCommand.builder()
//                .name("Maciej")
//                .lastname("Rozgocki")
//                .email("mrozgocki@gmail.com").build();
//
//        String cstudentcRequest = mapper.writeValueAsString(cstudentc);
//
//        String cstudentcResponse = postman.perform(post("/api/v1/students")
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
//                        .contentType(APPLICATION_JSON)
//                        .content(cecRequest))
//                .andExpect(status().isCreated())
//                .andReturn().getResponse().getContentAsString();
//
//        Exam exam = mapper.readValue(cecResponse, Exam.class);
//
//        String getExam = postman.perform(get("/api/v1/exams/" + exam.getId()))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        Exam finalExam = mapper.readValue(getExam,Exam.class);
//
//        assertEquals(studentWithSubject, finalExam.getStudent());
//        assertEquals("pierwszy", finalExam.getDescription());
//        assertEquals(2, finalExam.getQuestions().size());
//    }
//
//}