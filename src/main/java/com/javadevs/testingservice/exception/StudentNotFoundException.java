package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class StudentNotFoundException extends RuntimeException {
    long id;
}
