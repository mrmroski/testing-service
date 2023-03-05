package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.model.ExamResult;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ExamResultDto {

    private long id;
    private StudentDto student;
    private LocalDate createdAt;
    private String description;
    private Set<QuestionDto> questions;
    private double percentageResult;
    private long timeSpent;

    public ExamResultDto(ExamResult src) {
        this.id = src.getId();
        this.percentageResult = src.getPercentageResult();
        this.timeSpent = src.getTimeSpent();
        this.student = new StudentDto(src.getStudent());
        this.createdAt = src.getCreatedAt();
        this.description = src.getDescription();
        this.questions = src.getQuestions().stream().map(QuestionDto::new).collect(Collectors.toSet());
    }
}
