package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT DISTINCT e FROM Exam e LEFT JOIN FETCH e.student s LEFT JOIN FETCH s.studentSubjects ss LEFT JOIN FETCH ss.subject" +
            " LEFT JOIN FETCH e.questionExams qe LEFT JOIN FETCH qe.question q LEFT JOIN FETCH q.answers LEFT JOIN FETCH q.subject WHERE e.id=?1")
    Optional<Exam> findExamById(Long id);

    @Query(value = "SELECT DISTINCT e FROM Exam e LEFT JOIN FETCH e.student s LEFT JOIN FETCH s.studentSubjects ss LEFT JOIN FETCH ss.subject" +
            " LEFT JOIN FETCH e.questionExams qe LEFT JOIN FETCH qe.question q LEFT JOIN FETCH q.answers LEFT JOIN FETCH q.subject",
            countQuery = "SELECT count(e) FROM Exam e"
    )
    Page<Exam> findAllExams(Pageable pageable);

    @Modifying
    @Query("UPDATE Exam e SET e.deleted=true WHERE e.id=?1")
    void deleteByStudentId(Long id);
}
