package com.javadevs.testingservice.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class QuestionDto extends RepresentationModel<QuestionDto> {

    protected long id;
    protected String question;
    protected SubjectDto subject;
    protected Long version;

}
