package com.javadevs.testingservice.model.command.edit;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class EditSubjectCommand {

    private String subject;
    private String description;
    @NotNull(message = "VERSION_NOT_EMPTY")
    private Long version;
}
