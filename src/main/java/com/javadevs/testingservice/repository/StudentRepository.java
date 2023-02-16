package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.subjectsCovered WHERE s.id=?1")
    Optional<Student> findByIdWithSubjects(long id);
}
