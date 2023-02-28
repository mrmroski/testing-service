package com.javadevs.testingservice.model.command.edit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class EditStudentCommand {
    private String name;
    private String lastname;
    private String email;
    @NotNull(message = "VERSION_NOT_NULL")
    @PositiveOrZero(message = "VERSION_NOT_NEGATIVE")
    private Long version;
}
