package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.QuestionExam;
import com.javadevs.testingservice.model.StudentSubject;
import com.javadevs.testingservice.model.dto.AnswerDto;
import com.javadevs.testingservice.model.dto.ExamDto;
import com.javadevs.testingservice.model.dto.QuestionDto;
import com.javadevs.testingservice.model.dto.StudentDto;
import com.javadevs.testingservice.model.dto.SubjectDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ExamToExamDtoConverter implements Converter<Exam, ExamDto> {
    @Override
    public ExamDto convert(MappingContext<Exam, ExamDto> mappingContext) {
        Exam exam = mappingContext.getSource();
        return ExamDto.builder()
                .description(exam.getDescription())
                .createdAt(exam.getCreatedAt())
                .id(exam.getId())
                .version(exam.getVersion())
                .student(StudentDto.builder()
                        .id(exam.getStudent().getId())
                        .version(exam.getStudent().getVersion())
                        .email(exam.getStudent().getEmail())
                        .name(exam.getStudent().getName())
                        .lastname(exam.getStudent().getLastname())
                        .startedAt(exam.getCreatedAt())
                        .subjects(exam.getStudent().getStudentSubjects()
                                .stream()
                                .map(StudentSubject::getSubject)
                                .map(sub ->
                                        SubjectDto.builder()
                                                .version(sub.getVersion())
                                                .id(sub.getId())
                                                .subject(sub.getSubject())
                                                .description(sub.getDescription())
                                                .build()
                                ).collect(Collectors.toSet()))
                        .build()
                )
                .questions(exam.getQuestionExams()
                        .stream()
                        .map(QuestionExam::getQuestion)
                        .map(q -> QuestionDto.builder()
                                .question(q.getQuestion())
                                .id(q.getId())
                                .version(q.getVersion())
                                .subject(SubjectDto.builder()
                                        .subject(q.getSubject().getSubject())
                                        .id(q.getSubject().getId())
                                        .description(q.getSubject().getDescription())
                                        .version(q.getSubject().getVersion())
                                        .build()
                                )
                                .answers(q.getAnswers().stream()
                                        .map(a -> AnswerDto.builder()
                                                .correct(a.getCorrect())
                                                .answer(a.getAnswer())
                                                .id(a.getId())
                                                .version(a.getVersion())
                                                .build()
                                        )
                                        .collect(Collectors.toSet())
                                )
                                .build()
                        )
                        .collect(Collectors.toSet())
                )
                .build();
    }
}
