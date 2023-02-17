package com.javadevs.testingservice.model.command.create;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@Builder
public class CreateExamCommand {

    @NotEmpty(message = "STUDENT_ID_NOT_EMPTY")
    private Long studentId;
    @NotEmpty(message = "DESCRIPTION_NOT_EMPTY")
    private String description;
    @NotEmpty(message = "LIST_OF_QUESTIONS_NOT_EMPTY")
    private Set<Long> questions;
}
