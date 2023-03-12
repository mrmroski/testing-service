package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class ExamExpiredException extends RuntimeException {
    Long id;
}
