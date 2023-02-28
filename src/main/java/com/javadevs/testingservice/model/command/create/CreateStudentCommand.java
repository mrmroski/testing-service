package com.javadevs.testingservice.model.command.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
