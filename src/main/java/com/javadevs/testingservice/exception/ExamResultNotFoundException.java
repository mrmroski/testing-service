package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class ExamResultNotFoundException extends RuntimeException {
    Long id;
}
