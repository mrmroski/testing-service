package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    //@Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.studentSubjects ss LEFT JOIN FETCH ss.subject LEFT JOIN FETCH s.exams e LEFT JOIN FETCH e.questions q LEFT JOIN FETCH q.answers WHERE s.id=?1")
    //Optional<Student> findOneFetchAll(long id);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.studentSubjects ss LEFT JOIN FETCH ss.subject WHERE s.id=?1")
    Optional<Student> findOneWithSubjects(Long id);

    @Query("SELECT DISTINCT s FROM Student s WHERE s.id=?1")
    Optional<Student> findOne(long id);

//    @Query(value = "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.studentSubjects ss LEFT JOIN FETCH ss.subject LEFT JOIN FETCH s.exams e LEFT JOIN FETCH e.student LEFT JOIN FETCH e.questions q LEFT JOIN FETCH q.subject LEFT JOIN FETCH q.answers",
//            countQuery = "SELECT COUNT(s) FROM Student s"
//    )
    //Page<Student> findAllFetchAll(Pageable pageable);

    @Query(value = "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.studentSubjects ss LEFT JOIN FETCH ss.subject",
            countQuery = "SELECT COUNT(s) FROM Student s"
    )
    Page<Student> findAllWithSubjects(Pageable pageable);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.studentSubjects c WHERE c.id=?1")
    List<Student> findBySubjectId(long id);
}
