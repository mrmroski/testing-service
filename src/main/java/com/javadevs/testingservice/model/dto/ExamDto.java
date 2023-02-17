package com.javadevs.testingservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDto {

    private long id;
    private long studentId;
    private LocalDate createdAt;
    private String description;
    private long version;
}
