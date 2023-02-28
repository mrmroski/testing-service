package com.javadevs.testingservice.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Builder
@Value
public class StudentDto extends RepresentationModel<StudentDto> {

    long id;
    String name;
    String lastname;
    String email;
    LocalDate startedAt;
    Set<SubjectDto> subjects;
    Long version;
}
