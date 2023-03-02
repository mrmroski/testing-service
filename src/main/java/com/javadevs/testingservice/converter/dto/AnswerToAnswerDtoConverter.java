package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.dto.AnswerDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class AnswerToAnswerDtoConverter implements Converter<Answer, AnswerDto> {
    @Override
    public AnswerDto convert(MappingContext<Answer, AnswerDto> mappingContext) {
        Answer answer = mappingContext.getSource();
        return new AnswerDto(answer);
    }
}
