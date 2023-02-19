package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class SubjectWasNotCoveredException extends RuntimeException {
    Long id;
}
