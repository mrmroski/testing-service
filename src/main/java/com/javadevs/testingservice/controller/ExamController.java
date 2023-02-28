package com.javadevs.testingservice.controller;

import com.javadevs.testingservice.model.command.create.CreateExamCommand;
import com.javadevs.testingservice.model.dto.ExamDto;
import com.javadevs.testingservice.service.ExamService;
import javax.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/exams")
public class ExamController {

    private final ExamService examService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ExamDto> saveExam(@RequestBody @Valid CreateExamCommand command) throws MessagingException {
        log.info("saveExam({})", command);

        return new ResponseEntity<>(modelMapper
                .map(examService.saveExam(command), ExamDto.class), HttpStatus.CREATED);
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ExamDto> findExamById(@PathVariable("examId") long examId) {
        log.info("findExamById({})", examId);

        return new ResponseEntity<>(modelMapper
                .map(examService.findExamById(examId), ExamDto.class), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ExamDto>> findAllExams(@PageableDefault Pageable pageable) {
        log.info("findAllExams({})", pageable);

        return new ResponseEntity<>(examService.findAllExams(pageable)
                .map(exam -> modelMapper.map(exam, ExamDto.class)), HttpStatus.OK);
    }

    @GetMapping("/accept-request/{examId}")
    public String sendEmailWithTest(@PathVariable("examId") long examId) throws MessagingException {
        log.info("sendEmailWithTest()");
        examService.sendExam(examId);
        return "Twój test został pomyślnie wysłany na twojego maila. POWODZENIA!";
    }
}
