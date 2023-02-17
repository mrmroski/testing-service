package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.subjectsCovered WHERE s.id=?1")
    Optional<Student> findById(long id);

    @Query(value = "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.subjectsCovered",
            countQuery = "SELECT COUNT(s) FROM Student s"
    )
    Page<Student> findAll(Pageable pageable);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.subjectsCovered c WHERE c.id=?1")
    List<Student> findBySubjectId(long id);
}
