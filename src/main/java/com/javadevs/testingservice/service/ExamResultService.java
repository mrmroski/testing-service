package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.ExamResultNotFoundException;
import com.javadevs.testingservice.model.ExamResult;
import com.javadevs.testingservice.repository.ExamResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamResultService {

    private final ExamResultRepository examResultRepository;

    @Transactional(readOnly = true)
    public ExamResult findExamResultById(long id) {
        return examResultRepository.findExamResultById(id)
                .orElseThrow(() -> new ExamResultNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<ExamResult> findAllExamResults(Pageable pageable) {
        return examResultRepository.findAllExamResults(pageable);
    }

}
