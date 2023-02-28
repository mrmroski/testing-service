package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.controller.QuestionController;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.dto.AnswerDto;
import com.javadevs.testingservice.model.dto.QuestionDto;
import com.javadevs.testingservice.model.dto.SubjectDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class QuestionToQuestionDtoConverter implements Converter<Question, QuestionDto> {
    @Override
    public QuestionDto convert(MappingContext<Question, QuestionDto> mappingContext) {
        Question question = mappingContext.getSource();
        QuestionDto dto =  QuestionDto.builder()
                .id(question.getId())
                .answers(question.getAnswers()
                        .stream()
                        .map(ans ->
                            AnswerDto.builder()
                                    .answer(ans.getAnswer())
                                    .correct(ans.getCorrect())
                                    .id(ans.getId())
                                    .version(ans.getVersion())
                                    .build()
                        )
                        .collect(Collectors.toSet()))
                .question(question.getQuestion())
                .subject(SubjectDto.builder()
                        .subject(question.getSubject().getSubject())
                        .description(question.getSubject().getDescription())
                        .id(question.getSubject().getId())
                        .version(question.getSubject().getVersion())
                        .build()
                )
                .version(question.getVersion())
                .build();

        dto.add(linkTo(methodOn(QuestionController.class).addAnswer(question.getId(),null)).withRel("add-answer"));
        dto.add(linkTo(methodOn(QuestionController.class).deleteAnswer(question.getId(),null)).withRel("delete-answer"));
        dto.add(linkTo(methodOn(QuestionController.class).editQuestionPartially(question.getId(),null)).withRel("edit-question"));
        dto.add(linkTo(methodOn(QuestionController.class).deleteQuestionById(question.getId())).withRel("delete-question"));

        return dto;
    }
}
