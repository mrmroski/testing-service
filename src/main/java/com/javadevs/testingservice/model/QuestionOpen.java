package com.javadevs.testingservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("OPEN")
@SQLDelete(sql = "UPDATE questions SET deleted = true WHERE question_id=? and version=?")
@Where(clause = "deleted=false")
public class QuestionOpen extends Question{
    private String answer;
}
