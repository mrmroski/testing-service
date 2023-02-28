package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.controller.StudentController;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.StudentSubject;
import com.javadevs.testingservice.model.dto.StudentDto;
import com.javadevs.testingservice.model.dto.SubjectDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class StudentToStudentDtoConverter implements Converter<Student, StudentDto> {
    @Override
    public StudentDto convert(MappingContext<Student, StudentDto> mappingContext) {
        Student student = mappingContext.getSource();
        Set<StudentSubject> sc = new HashSet<>();
        if (student.getStudentSubjects() != null) {
            sc = student.getStudentSubjects();
        }
        StudentDto dto =  StudentDto.builder()
                .id(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .lastname(student.getLastname())
                .startedAt(student.getStartedAt())
                .subjects(sc.stream().map(StudentSubject::getSubject)
                        .map(x ->
                            SubjectDto.builder()
                                    .description(x.getDescription())
                                    .subject(x.getSubject())
                                    .id(x.getId())
                                    .version(x.getVersion())
                                    .build()
                        )
                        .collect(Collectors.toSet())
                )
                .version(student.getVersion())
                .build();

        dto.add(linkTo(methodOn(StudentController.class).addSubjectCovered(student.getId(), null)).withRel("add-subject"));
        dto.add(linkTo(methodOn(StudentController.class).deleteSubject(student.getId(), null)).withRel("delete-subject"));
        dto.add(linkTo(methodOn(StudentController.class).editStudentPartially(student.getId(), null)).withRel("edit-student"));
        dto.add(linkTo(methodOn(StudentController.class).deleteStudentById(student.getId())).withRel("delete-student"));

        return dto;
    }
}
