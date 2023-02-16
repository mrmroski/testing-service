package com.javadevs.testingservice.model.command.create;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CreateAnswerCommand {

    private String answer;
    private Long questionId;
}
