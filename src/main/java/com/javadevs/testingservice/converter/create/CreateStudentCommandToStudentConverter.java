package com.javadevs.testingservice.converter.create;

import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateStudentCommandToStudentConverter implements Converter<CreateStudentCommand, Student> {


    @Override
    public Student convert(MappingContext<CreateStudentCommand, Student> mappingContext) {
        CreateStudentCommand command = mappingContext.getSource();
        return Student.builder()
                .name(command.getName())
                .lastname(command.getLastname())
                .email(command.getEmail())
                .startedAt(LocalDate.now())
                .build();
    }
}
