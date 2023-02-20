package com.javadevs.testingservice.model.command.edit;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class EditStudentCommand {

    private String name;
    private String lastname;
    private String email;
}
