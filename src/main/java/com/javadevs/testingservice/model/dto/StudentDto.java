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
public class StudentDto {

    private long id;
    private String name;
    private String lastname;
    private String email;
    private LocalDate startedAt;

}
