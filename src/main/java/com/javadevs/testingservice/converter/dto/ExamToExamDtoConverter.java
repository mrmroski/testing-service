package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.dto.ExamDto;
import com.javadevs.testingservice.model.dto.StudentDto;
import com.javadevs.testingservice.model.dto.SubjectDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ExamToExamDtoConverter implements Converter<Exam, ExamDto> {
    @Override
    public ExamDto convert(MappingContext<Exam, ExamDto> mappingContext) {
        Exam exam = mappingContext.getSource();
        return ExamDto.builder()
                .description(exam.getDescription())
                .createdAt(exam.getCreatedAt())
                .id(exam.getId())
                .version(exam.getVersion())
                .student(StudentDto.builder()
                        .id(exam.getStudent().getId())
                        .email(exam.getStudent().getEmail())
                        .name(exam.getStudent().getName())
                        .lastname(exam.getStudent().getLastname())
                        .startedAt(exam.getCreatedAt())
                        .subjects(exam.getStudent().getSubjectsCovered()
                                .stream()
                                .map(sub ->
                                        SubjectDto.builder()
                                                .id(sub.getId())
                                                .subject(sub.getSubject())
                                                .description(sub.getDescription())
                                                .build()
                                ).collect(Collectors.toSet()))
                        .build()
                )
                .build();
    }
}
