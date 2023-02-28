package com.javadevs.testingservice.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Builder
@Value
public class QuestionDto extends RepresentationModel<QuestionDto> {

    long id;
    String question;
    SubjectDto subject;
    Set<AnswerDto> answers;
    Long version;
}
