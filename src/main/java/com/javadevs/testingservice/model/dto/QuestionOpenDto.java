package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.controller.QuestionController;
import com.javadevs.testingservice.model.QuestionOpen;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionOpenDto extends QuestionDto {

    private String answer;

    public QuestionOpenDto(QuestionOpen src) {
        this.id = src.getId();
        this.question = src.getQuestion();
        this.subject = new SubjectDto(src.getSubject());
        this.version = src.getVersion();
        this.answer = src.getAnswer();

        this.add(linkTo(methodOn(QuestionController.class).addAnswer(src.getId(), null)).withRel("add-answer"));
        this.add(linkTo(methodOn(QuestionController.class).deleteAnswer(src.getId(), null)).withRel("delete-answer"));
        this.add(linkTo(methodOn(QuestionController.class).editQuestionPartially(src.getId(), null)).withRel("edit-question"));
        this.add(linkTo(methodOn(QuestionController.class).deleteQuestionById(src.getId())).withRel("delete-question"));
    }
}
