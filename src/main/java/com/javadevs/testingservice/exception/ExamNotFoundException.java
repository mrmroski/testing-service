package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class ExamNotFoundException extends RuntimeException {
    long id;
}
