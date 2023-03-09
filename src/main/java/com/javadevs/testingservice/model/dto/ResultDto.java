package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.model.Result;
import lombok.Data;

@Data
public class ResultDto {

    private long id;
    private double percent;
    private long time;
    private long tryNumber;

    public ResultDto(Result src) {
        this.id = src.getId();
        this.percent = src.getPercentageResult();
        this.time = src.getTimeSpent();
        this.tryNumber = src.getTryNumber();
    }
}
