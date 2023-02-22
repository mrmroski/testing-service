package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.dto.AnswerDto;
import com.javadevs.testingservice.model.dto.QuestionDto;
import com.javadevs.testingservice.model.dto.SubjectDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class QuestionToQuestionDtoConverter implements Converter<Question, QuestionDto> {
    @Override
    public QuestionDto convert(MappingContext<Question, QuestionDto> mappingContext) {
        Question question = mappingContext.getSource();
        return QuestionDto.builder()
                .id(question.getId())
                .answers(question.getAnswers()
                        .stream()
                        .map(ans -> {
                            AnswerDto dto = new AnswerDto();
                            dto.setAnswer(ans.getAnswer());
                            dto.setCorrect(ans.getCorrect());
                            dto.setId(ans.getId());
                            return dto;
                        })
                        .collect(Collectors.toSet()))
                .question(question.getQuestion())
                .subject(SubjectDto.builder()
                        .subject(question.getSubject().getSubject())
                        .description(question.getSubject().getDescription())
                        .id(question.getSubject().getId())
                        .build()
                )
                .questionType(question.getQuestionType())
                .version(question.getVersion())
                .build();
    }
}