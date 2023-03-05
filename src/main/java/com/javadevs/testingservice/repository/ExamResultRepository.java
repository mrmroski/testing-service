package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.ExamResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {

    @Query("SELECT DISTINCT er FROM ExamResult er LEFT JOIN FETCH er.student s LEFT JOIN FETCH s.subjects WHERE er.id=?1")
    Optional<ExamResult> findExamResultById(Long id);

    @Query(value = "SELECT DISTINCT er FROM ExamResult er LEFT JOIN FETCH er.student s LEFT JOIN FETCH s.subjects",
            countQuery = "SELECT count(er) FROM ExamResult er"
    )
    Page<ExamResult> findAllExamResults(Pageable pageable);

}
