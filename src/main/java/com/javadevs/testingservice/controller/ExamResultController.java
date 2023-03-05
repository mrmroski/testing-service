package com.javadevs.testingservice.controller;

import com.javadevs.testingservice.model.dto.ExamResultDto;
import com.javadevs.testingservice.service.ExamResultService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/exam-results")
public class ExamResultController {

    private final ExamResultService examResultService;
    private final ModelMapper modelMapper;

    @GetMapping("/{examResultId}")
    public ResponseEntity<ExamResultDto> findExamResultById(@PathVariable("examResultId") long examResultId) {
        log.info("findExamResultById({})", examResultId);

        return new ResponseEntity<>(modelMapper
                .map(examResultService.findExamResultById(examResultId), ExamResultDto.class), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ExamResultDto>> findAllExamResults(@PageableDefault Pageable pageable) {
        log.info("findAllExamResults({})", pageable);

        return new ResponseEntity<>(examResultService.findAllExamResults(pageable)
                .map(examResult -> modelMapper.map(examResult, ExamResultDto.class)), HttpStatus.OK);
    }

}
