package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.subject LEFT JOIN FETCH q.answers WHERE q.id=?1")
    Optional<Question> findById(long id);

    @Query(value = "SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.subject LEFT JOIN FETCH q.answers",
            countQuery = "SELECT COUNT(q) FROM Question q")
    Page<Question> findAllWithAnswers(Pageable pageable);

    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.subject LEFT JOIN FETCH q.answers WHERE q.id in :ids")
    Set<Question> findQuestionsByIds(@Param("ids") Set<Long> ids);
}
