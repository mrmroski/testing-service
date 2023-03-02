package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.model.Exam;
import liquibase.pro.packaged.E;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ExamDto {

    private long id;
    private StudentDto student;
    private LocalDate createdAt;
    private String description;
    private Set<QuestionDto> questions;

    public ExamDto(Exam src) {
        this.id = src.getId();
        this.student = new StudentDto(src.getStudent());
        this.createdAt = src.getCreatedAt();
        this.description = src.getDescription();
        this.questions = src.getQuestions().stream().map(QuestionDto::new).collect(Collectors.toSet());
    }
}
