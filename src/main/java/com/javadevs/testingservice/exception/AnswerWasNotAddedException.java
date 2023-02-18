package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class AnswerWasNotAddedException extends RuntimeException {
    long id;
}
