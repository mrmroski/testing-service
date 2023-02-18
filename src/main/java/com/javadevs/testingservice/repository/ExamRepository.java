package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT DISTINCT e FROM Exam e LEFT JOIN FETCH e.student s LEFT JOIN FETCH s.subjectsCovered" +
            " LEFT JOIN FETCH e.questions q LEFT JOIN FETCH q.answers WHERE e.id=?1")
    Optional<Exam> findExamById(Long id);

    @Query(value = "SELECT DISTINCT e FROM Exam e LEFT JOIN FETCH e.student s LEFT JOIN FETCH s.subjectsCovered" +
            " LEFT JOIN FETCH e.questions q LEFT JOIN FETCH q.answers",
            countQuery = "SELECT count(e) FROM Exam e"
    )
    Page<Exam> findAll(Pageable pageable);
}
