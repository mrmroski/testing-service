package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class AnswerIsAlreadyAddedException extends RuntimeException{
    Long id;
}
