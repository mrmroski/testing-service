package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.dto.StudentDto;
import com.javadevs.testingservice.model.dto.SubjectDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentToStudentDtoConverter implements Converter<Student, StudentDto> {
    @Override
    public StudentDto convert(MappingContext<Student, StudentDto> mappingContext) {
        Student student = mappingContext.getSource();
        Set<Subject> sc = new HashSet<>();
        if (student.getSubjectsCovered() != null) {
            sc = student.getSubjectsCovered();
        }
        return StudentDto.builder()
                .id(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .lastname(student.getLastname())
                .startedAt(student.getStartedAt())
                .subjects(sc.stream()
                        .map(x -> {
                            SubjectDto dto = SubjectDto.builder()
                                    .subject(x.getSubject())
                                    .description(x.getDescription())
                                    .id(x.getId())
                                    .build();
                            return dto;
                        }).collect(Collectors.toSet()))
                .version(student.getVersion())
                .build();
    }
}
