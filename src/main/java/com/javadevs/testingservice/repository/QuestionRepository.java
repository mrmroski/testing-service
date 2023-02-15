package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.subject LEFT JOIN FETCH q.answers WHERE q.id=?1")
    Optional<Question> findById(long id);
}
