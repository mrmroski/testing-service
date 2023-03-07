package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.AnswerWasNotAddedException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("CLOSED")
@SQLDelete(sql = "UPDATE questions SET deleted = true WHERE question_id=? and version=?")
@Where(clause = "deleted=false")
public class QuestionClosed extends Question{
    @OneToMany(mappedBy = "question", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Answer> answers;

    public void deleteAnswer(Long answerId) {
        Answer toDelete = this.answers.stream()
                .filter(curr -> curr.getId() == answerId)
                .findFirst()
                .orElseThrow(() -> new AnswerWasNotAddedException(answerId));
        this.answers.remove(toDelete);
    }
}
