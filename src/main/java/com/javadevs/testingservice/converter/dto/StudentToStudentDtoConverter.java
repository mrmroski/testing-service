package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.dto.StudentDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class StudentToStudentDtoConverter implements Converter<Student, StudentDto> {
    @Override
    public StudentDto convert(MappingContext<Student, StudentDto> mappingContext) {
        Student student = mappingContext.getSource();
        return new StudentDto(student);
    }
}
