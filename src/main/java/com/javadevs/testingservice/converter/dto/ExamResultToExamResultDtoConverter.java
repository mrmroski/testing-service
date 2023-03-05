package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.ExamResult;
import com.javadevs.testingservice.model.dto.ExamResultDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class ExamResultToExamResultDtoConverter implements Converter<ExamResult, ExamResultDto> {
    @Override
    public ExamResultDto convert(MappingContext<ExamResult, ExamResultDto> mappingContext) {
        ExamResult examResult = mappingContext.getSource();
        return new ExamResultDto(examResult);
    }
}
