package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.StudentSubject;
import com.javadevs.testingservice.model.StudentSubjectId;
import com.javadevs.testingservice.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface StudentSubjectRepository extends JpaRepository<StudentSubject, StudentSubjectId> {
    @Modifying
    @Transactional
    @Query("DELETE FROM StudentSubject ss WHERE ss.id.studentId=?1 AND ss.id.subjectId=?2")
    void deleteByIds(Long st, Long sub);

    @Modifying
    @Query("UPDATE StudentSubject ss SET ss.deleted=true WHERE ss.id.subjectId=?1")
    void softDeleteAllBySubjectId(Long subjectId);
}
