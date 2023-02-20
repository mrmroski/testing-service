package com.javadevs.testingservice.model.command.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class CreateStudentCommand {

    @NotEmpty(message = "NAME_NOT_EMPTY")
    private String name;
    @NotEmpty(message = "LASTNAME_NOT_EMPTY")
    private String lastname;
    @Email
    private String email;
}
