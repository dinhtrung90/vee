package com.vts.vee.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the VeeResponse entity.
 */
public class VeeResponseDTO implements Serializable {

    private Long id;

    private String answer;


    private Long questionId;

    private Long respondentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getRespondentId() {
        return respondentId;
    }

    public void setRespondentId(Long respondentId) {
        this.respondentId = respondentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VeeResponseDTO veeResponseDTO = (VeeResponseDTO) o;
        if (veeResponseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), veeResponseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VeeResponseDTO{" +
            "id=" + getId() +
            ", answer='" + getAnswer() + "'" +
            ", question=" + getQuestionId() +
            ", respondent=" + getRespondentId() +
            "}";
    }
}
