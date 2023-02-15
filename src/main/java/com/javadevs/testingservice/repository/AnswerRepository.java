package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
