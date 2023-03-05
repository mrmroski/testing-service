package com.javadevs.testingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "1")
public class ExamResult extends Exam {

    @Column(name = "percentage_result")
    private double percentageResult;
    @Column(name = "time_spent")
    private long timeSpent;

}
