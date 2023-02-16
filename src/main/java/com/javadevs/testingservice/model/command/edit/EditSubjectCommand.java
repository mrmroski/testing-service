package com.javadevs.testingservice.model.command.edit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditSubjectCommand {

    private String subject;
    private String description;
    private Long version;
}
