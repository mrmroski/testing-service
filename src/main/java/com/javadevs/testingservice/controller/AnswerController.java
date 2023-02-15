package com.javadevs.testingservice.controller;

import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.command.CreateAnswerCommand;
import com.javadevs.testingservice.model.dto.AnswerDto;
import com.javadevs.testingservice.service.AnswerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/answers")
public class AnswerController {

    private final AnswerService answerService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Answer> saveAnswer(@RequestBody @Valid CreateAnswerCommand command) {
        log.info("saveAnswer{}", command);
        return new ResponseEntity(modelMapper
                .map(answerService.saveAnswer(command),AnswerDto.class),HttpStatus.CREATED);
    }

    @GetMapping("/{answerId}")
    public ResponseEntity findAnswerById(@PathVariable("answerId") long answerId) {
        log.info("findAnswerById({})", answerId);
        return new ResponseEntity(modelMapper
                .map(answerService.findAnswerById(answerId),AnswerDto.class),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity findAllAnswers(@PageableDefault Pageable pageable) {
        log.info("findAllAnswers()");
        return new ResponseEntity(answerService.findAllAnswers(pageable)
                .map(answer -> modelMapper.map(answer, AnswerDto.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity deleteAnswerById(@PathVariable("answerId") long answerId) {
        log.info("deleteAnswerById({})", answerId);
        answerService.deleteAnswer(answerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
