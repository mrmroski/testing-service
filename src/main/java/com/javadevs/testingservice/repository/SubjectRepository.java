package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("SELECT DISTINCT s FROM Subject s WHERE s.id=?1")
    Optional<Subject> findSubjectById(long id);

    @Query("SELECT DISTINCT s FROM Subject s LEFT JOIN FETCH s.studentSubjects ss LEFT JOIN FETCH ss.student WHERE s.id=?1")
    Optional<Subject> findOneWithStudents(Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Subject s SET s.subject = :subject, s.description = :desc, s.version = :version WHERE s.id=:id"
    )
    int updateSubject(@Param("id") long subject_id, @Param("version") long version, @Param("subject") String subject, @Param("desc") String description);
}
