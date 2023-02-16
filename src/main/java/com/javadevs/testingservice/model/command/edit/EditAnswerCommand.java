package com.javadevs.testingservice.model.command.edit;

import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditAnswerCommand {

    private String answer;
    private Long version;
    private Long questionId;
}
