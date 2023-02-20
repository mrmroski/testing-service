package com.javadevs.testingservice.model.command.edit;

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
}
