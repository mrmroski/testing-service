package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class ExamAlreadyAssignedException extends RuntimeException {
    Long id;
}
