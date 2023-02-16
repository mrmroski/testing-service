package com.javadevs.testingservice.model.command.create;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CreateSubjectCommand {

    @NotEmpty(message = "SUBJECT_NOT_EMPTY")
    private String subject;
    @NotEmpty(message = "DESCRIPTION_NOT_EMPTY")
    private String description;
}
