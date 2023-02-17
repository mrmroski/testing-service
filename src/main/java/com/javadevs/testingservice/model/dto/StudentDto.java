package com.javadevs.testingservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private Set<SubjectDto> subjects;
    private long version;
}
