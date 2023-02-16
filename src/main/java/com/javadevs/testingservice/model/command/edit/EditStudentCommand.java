package com.javadevs.testingservice.model.command.edit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditStudentCommand {

    private String name;
    private String lastname;
    private String email;
    private LocalDate startedAt;
    private Long version;
}
