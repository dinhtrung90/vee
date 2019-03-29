package com.vts.vee.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the QuestionType entity.
 */
public class QuestionTypeDTO implements Serializable {

    private Long id;

    private String name;


    private Long questionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionTypeDTO questionTypeDTO = (QuestionTypeDTO) o;
        if (questionTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), questionTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuestionTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", question=" + getQuestionId() +
            "}";
    }
}
