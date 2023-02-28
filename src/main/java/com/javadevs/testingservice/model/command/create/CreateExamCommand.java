package com.javadevs.testingservice.model.command.create;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
public class CreateExamCommand {

    @Positive(message = "STUDENT_ID_NOT_NEGATIVE")
    private Long studentId;
    @NotEmpty(message = "DESCRIPTION_NOT_EMPTY")
    private String description;
    @NotNull(message = "LIST_OF_QUESTIONS_NOT_NULL")
    private Set<Long> questions;
}
