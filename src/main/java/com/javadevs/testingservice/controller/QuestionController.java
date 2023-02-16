package com.javadevs.testingservice.controller;

import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.command.create.CreateQuestionCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
import com.javadevs.testingservice.model.dto.QuestionDto;
import com.javadevs.testingservice.service.QuestionService;
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
@RequestMapping("api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Question> saveQuestion(@RequestBody @Valid CreateQuestionCommand command) {
        log.info("saveQuestion{}", command);
        return new ResponseEntity(modelMapper
                .map(questionService.saveQuestion(command), QuestionDto.class), HttpStatus.CREATED);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity findQuestionById(@PathVariable("questionId") long questionId) {
        log.info("findQuestionById({})", questionId);
        return new ResponseEntity(modelMapper
                .map(questionService.findQuestionById(questionId), QuestionDto.class), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity findAllQuestions(@PageableDefault Pageable pageable) {
        log.info("findAllQuestions()");
        return new ResponseEntity(questionService.findAllQuestions(pageable)
                .map(question -> modelMapper.map(question, QuestionDto.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity deleteQuestionById(@PathVariable("questionId") long questionId) {
        log.info("deleteQuestionById({})", questionId);
        questionService.deleteQuestion(questionId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity editQuestionPartially(@PathVariable("questionId") long questionId, @RequestBody EditQuestionCommand command) {
        log.info("editQuestionPartially({}, {})", questionId, command);
        Question question = questionService.editQuestionPartially(questionId, command);
        return new ResponseEntity(modelMapper.map(question, QuestionDto.class), HttpStatus.OK);
    }
}
