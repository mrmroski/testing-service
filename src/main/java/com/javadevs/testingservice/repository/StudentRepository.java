package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.subjects WHERE s.id=?1")
    Optional<Student> findOneWithSubjects(Long id);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.exams WHERE s.id=?1")
    Optional<Student> findOneWithExams(Long id);

    @Query("SELECT DISTINCT s FROM Student s WHERE s.id=?1")
    Optional<Student> findOne(long id);

    @Query(value = "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.subjects",
            countQuery = "SELECT COUNT(s) FROM Student s"
    )
    Page<Student> findAllWithSubjects(Pageable pageable);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.subjects c WHERE c.id=?1")
    List<Student> findBySubjectId(long id);
}
