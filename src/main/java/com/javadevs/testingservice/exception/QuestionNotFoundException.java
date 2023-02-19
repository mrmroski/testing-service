package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class QuestionNotFoundException extends RuntimeException {
    long id;
}
