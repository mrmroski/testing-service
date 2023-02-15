package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.dto.StudentDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class StudentToStudentDtoConverter implements Converter<Student, StudentDto> {
    @Override
    public StudentDto convert(MappingContext<Student, StudentDto> mappingContext) {
        Student student = mappingContext.getSource();
        return StudentDto.builder()
                .id(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .lastname(student.getLastname())
                .startedAt(student.getStartedAt())
                .build();
    }
}
