package com.javadevs.testingservice.exception;

import lombok.Value;

@Value
public class WrongQuestionTypeException extends RuntimeException{
    String typeOfClass;
}
