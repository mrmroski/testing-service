package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
