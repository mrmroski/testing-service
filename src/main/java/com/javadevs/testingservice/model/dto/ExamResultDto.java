package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.model.ExamResult;
import lombok.Data;

@Data
public class ExamResultDto {

    private long id;
    private StudentDto student;
    private double percentageResult;
    private long timeSpent;

    public ExamResultDto(ExamResult src) {
        this.id = src.getId();
        this.student = new StudentDto(src.getStudent());
        this.percentageResult = src.getPercentageResult();
        this.timeSpent = src.getTimeSpent();
    }
}
