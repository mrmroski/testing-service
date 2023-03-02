package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.controller.QuestionController;
import com.javadevs.testingservice.model.Question;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.RepresentationModel;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionDto extends RepresentationModel<QuestionDto> {

    private long id;
    private String question;
    private SubjectDto subject;
    private Set<AnswerDto> answers;
    private Long version;

    public QuestionDto(Question src) {
        this.id = src.getId();
        this.question = src.getQuestion();
        this.subject = new SubjectDto(src.getSubject());
        this.answers = src.getAnswers().stream().map(AnswerDto::new).collect(Collectors.toSet());
        this.version = src.getVersion();

        this.add(linkTo(methodOn(QuestionController.class).addAnswer(src.getId(),null)).withRel("add-answer"));
        this.add(linkTo(methodOn(QuestionController.class).deleteAnswer(src.getId(),null)).withRel("delete-answer"));
        this.add(linkTo(methodOn(QuestionController.class).editQuestionPartially(src.getId(),null)).withRel("edit-question"));
        this.add(linkTo(methodOn(QuestionController.class).deleteQuestionById(src.getId())).withRel("delete-question"));
    }
}
