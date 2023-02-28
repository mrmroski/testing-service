package com.javadevs.testingservice.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Value
@Builder
public class ExamDto extends RepresentationModel<ExamDto> {

    long id;
    StudentDto student;
    LocalDate createdAt;
    String description;
    Set<QuestionDto> questions;
    Long version;
}
