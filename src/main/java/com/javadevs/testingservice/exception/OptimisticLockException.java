package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class OptimisticLockException extends RuntimeException {
    Long id;
    Long currentVersion;
    Long providedVersion;
}
