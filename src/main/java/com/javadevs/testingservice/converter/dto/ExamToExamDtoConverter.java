package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.dto.ExamDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class ExamToExamDtoConverter implements Converter<Exam, ExamDto> {
    @Override
    public ExamDto convert(MappingContext<Exam, ExamDto> mappingContext) {
        Exam exam = mappingContext.getSource();
        return new ExamDto(exam);
    }
}
