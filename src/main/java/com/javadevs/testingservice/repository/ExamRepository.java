package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT DISTINCT e FROM Exam e LEFT JOIN FETCH e.student LEFT JOIN FETCH e.questions WHERE e.id=?1")
    Optional<Exam> findExamById(Long id);
}
