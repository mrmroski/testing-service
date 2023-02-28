package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.QuestionExam;
import com.javadevs.testingservice.model.QuestionExamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.Set;

public interface QuestionExamRepository extends JpaRepository<QuestionExam, QuestionExamId> {
    @Modifying
    @Query("UPDATE QuestionExam qe SET qe.deleted=true WHERE qe.id.questionId=?1")
    void softDeleteAllByQuestionId(Long questionId);

    @Modifying
    @Query("UPDATE QuestionExam qe SET qe.deleted=true WHERE qe.id.questionId IN ?1")
    void softDeleteAllByQuestionIds(Set<Long> ids);
}
