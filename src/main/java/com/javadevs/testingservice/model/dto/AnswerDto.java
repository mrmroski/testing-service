package com.javadevs.testingservice.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Value
@Builder
public class AnswerDto extends RepresentationModel<AnswerDto> {

    long id;
    String answer;
    Boolean correct;
    Long version;
}
