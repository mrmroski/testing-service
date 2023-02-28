package com.javadevs.testingservice.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Builder
@Value
public class SubjectDto extends RepresentationModel<SubjectDto> {

    long id;
    String subject;
    String description;
    Long version;
}
