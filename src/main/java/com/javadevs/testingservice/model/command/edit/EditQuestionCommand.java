package com.javadevs.testingservice.model.command.edit;

import com.javadevs.testingservice.model.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditQuestionCommand {

    private String question;
    private String correctAnswer;
    private QuestionType questionType;
    private Long version;
    private Long subjectId;
}
