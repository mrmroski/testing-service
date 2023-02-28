package com.javadevs.testingservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDto {

    private long id;
    private StudentDto student;
    private LocalDate createdAt;
    private String description;
    private Set<QuestionDto> questions;
    private Long version;
}
