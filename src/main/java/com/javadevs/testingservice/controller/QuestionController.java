package com.javadevs.testingservice.controller;

import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.QuestionClosed;
import com.javadevs.testingservice.model.QuestionOpen;
import com.javadevs.testingservice.model.command.create.CreateQuestionClosedCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionOpenCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
import com.javadevs.testingservice.model.command.questionEdit.AddAnswerCommand;
import com.javadevs.testingservice.model.command.questionEdit.DeleteAnswerCommand;
import com.javadevs.testingservice.model.dto.QuestionClosedDto;
import com.javadevs.testingservice.model.dto.QuestionDto;
import com.javadevs.testingservice.model.dto.QuestionOpenDto;
import com.javadevs.testingservice.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final ModelMapper modelMapper;

    @PostMapping("/closed")
    public ResponseEntity<QuestionDto> saveQuestionClosed(@RequestBody @Valid CreateQuestionClosedCommand command) {
        log.info("saveQuestionClosed{}", command);

        return new ResponseEntity<>(
                modelMapper.map(questionService.saveQuestionClosed(command), QuestionClosedDto.class),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/open")
    public ResponseEntity<QuestionDto> saveQuestionOpen(@RequestBody @Valid CreateQuestionOpenCommand command) {
        log.info("saveQuestionOpened{}", command);

        return new ResponseEntity<>(
                modelMapper.map(questionService.saveQuestionOpen(command), QuestionOpenDto.class),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDto> findQuestionById(@PathVariable("questionId") long questionId) {
        log.info("findQuestionById({})", questionId);
        Question q = questionService.findQuestionById(questionId);

        if (q instanceof QuestionOpen) {
            return new ResponseEntity<>(modelMapper
                    .map(q, QuestionOpenDto.class),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(modelMapper
                    .map(q, QuestionClosedDto.class),
                    HttpStatus.OK
            );
        }
    }

    @GetMapping
    public ResponseEntity<Page<QuestionDto>> findAllQuestions(@PageableDefault Pageable pageable) {
        log.info("findAllQuestions()");

        return new ResponseEntity<>(questionService.findAllQuestions(pageable)
                .map(question -> {
                    if (question instanceof QuestionClosed) {
                        return modelMapper.map(question, QuestionClosedDto.class);
                    } else {
                        return modelMapper.map(question, QuestionOpenDto.class);
                    }
                }),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable("questionId") long questionId) {
        log.info("deleteQuestionById({})", questionId);

        questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{questionId}")
    public ResponseEntity<QuestionDto> editQuestionPartially(@PathVariable("questionId") long id,
                                                             @RequestBody @Valid EditQuestionCommand cmd) {
        log.info("editQuestionPartially({})", id);

        Question q = questionService.editQuestionPartially(id, cmd);

        if (q instanceof QuestionOpen) {
            return new ResponseEntity<>(modelMapper
                    .map(q, QuestionOpenDto.class),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(modelMapper
                    .map(q, QuestionClosedDto.class),
                    HttpStatus.OK
            );
        }
    }

    @PatchMapping("/{questionId}/addAnswer")
    public ResponseEntity<QuestionDto> addAnswer(@PathVariable("questionId") long id,
                                                 @RequestBody @Valid AddAnswerCommand cmd) {
        log.info("addAnswer({})", id);

        Question q = questionService.addAnswer(cmd);
        return new ResponseEntity<>(modelMapper.map(q, QuestionClosedDto.class), HttpStatus.OK);
    }

    @PatchMapping("/{questionId}/deleteAnswer")
    public ResponseEntity<?> deleteAnswer(@PathVariable("questionId") long id,
                                          @RequestBody @Valid DeleteAnswerCommand cmd) {
        log.info("deleteAnswer({})", id);

        questionService.deleteAnswer(cmd);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
