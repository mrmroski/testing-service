package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.dto.QuestionDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class QuestionToQuestionDtoConverter implements Converter<Question, QuestionDto> {
    @Override
    public QuestionDto convert(MappingContext<Question, QuestionDto> mappingContext) {
       Question question = mappingContext.getSource();
       return QuestionDto.builder()
               .id(question.getId())
               .correctAnswer(question.getCorrectAnswer())
               .question(question.getQuestion())
               .subject(question.getSubject().getSubject())
               .questionType(question.getQuestionType())
               .build();
    }
}
