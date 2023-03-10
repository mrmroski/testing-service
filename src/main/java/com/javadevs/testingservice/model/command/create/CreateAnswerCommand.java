package com.javadevs.testingservice.model.command.create;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
public class CreateAnswerCommand {

    @NotEmpty(message = "ANSWER_NOT_EMPTY")
    private String answer;
    @NotNull(message = "CORRECT_NOT_EMPTY")
    private Boolean correct;
}
