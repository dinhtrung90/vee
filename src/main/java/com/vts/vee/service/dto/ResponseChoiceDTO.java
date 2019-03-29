package com.vts.vee.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ResponseChoice entity.
 */
public class ResponseChoiceDTO implements Serializable {

    private Long id;

    private String text;


    private Long questionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

        ResponseChoiceDTO responseChoiceDTO = (ResponseChoiceDTO) o;
        if (responseChoiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), responseChoiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResponseChoiceDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", question=" + getQuestionId() +
            "}";
    }
}
