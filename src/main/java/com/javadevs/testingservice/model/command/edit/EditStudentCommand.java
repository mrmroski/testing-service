package com.javadevs.testingservice.model.command.edit;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
public class EditStudentCommand {
    private String name;
    private String lastname;
    private String email;
    private LocalDate startedAt;
    @NotNull(message = "VERSION_NOT_EMPTY")
    private Long version;
}
