package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class SubjectNotFoundException extends RuntimeException {
    long id;
}
