package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
