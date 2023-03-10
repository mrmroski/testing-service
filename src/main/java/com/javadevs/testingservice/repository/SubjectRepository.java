package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("SELECT DISTINCT s FROM Subject s WHERE s.id=?1")
    Optional<Subject> findSubjectById(long id);

    @Query("SELECT DISTINCT s FROM Subject s WHERE s.id=?1")
    Optional<Subject> findOneWithStudents(Long id);
}
