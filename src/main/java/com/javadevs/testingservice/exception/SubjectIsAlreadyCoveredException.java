package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class SubjectIsAlreadyCoveredException extends RuntimeException {
    Long id;
}
