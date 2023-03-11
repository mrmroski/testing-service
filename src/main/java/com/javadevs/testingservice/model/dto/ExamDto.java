package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.QuestionClosed;
import com.javadevs.testingservice.model.QuestionOpen;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ExamDto {

    private long id;
    private StudentDto student;
    private LocalDate createdAt;
    private String description;
    private Set<QuestionDto> questions;
    private Set<ResultDto> results;

    public ExamDto(Exam src) {
        this.id = src.getId();
        this.student = new StudentDto(src.getStudent());
        this.createdAt = src.getCreatedAt();
        this.description = src.getDescription();
        this.questions = src.getQuestions().stream().map(q -> {
            if (q instanceof QuestionClosed) {
                return new QuestionClosedDto((QuestionClosed) q);
            } else {
                return new QuestionOpenDto((QuestionOpen) q);
            }
        }).collect(Collectors.toSet());

        if (src.getResults() != null) {
            this.results = src.getResults().stream().map(ResultDto::new).collect(Collectors.toSet());
        } else {
            this.results = new HashSet<>();
        }
    }
}
