package com.javadevs.testingservice.model.command.edit;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class EditSubjectCommand {
    @NotEmpty(message = "SUBJECT_NOT_EMPTY")
    private String subject;
    @NotEmpty(message = "DESCRIPTION_NOT_EMPTY")
    private String description;
}
