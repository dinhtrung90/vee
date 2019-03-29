package com.vts.vee.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SurveyResponse entity.
 */
public class SurveyResponseDTO implements Serializable {

    private Long id;

    private Instant updated;

    private Instant startedat;

    private Instant completedat;


    private Long surveyId;

    private Long respondentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getStartedat() {
        return startedat;
    }

    public void setStartedat(Instant startedat) {
        this.startedat = startedat;
    }

    public Instant getCompletedat() {
        return completedat;
    }

    public void setCompletedat(Instant completedat) {
        this.completedat = completedat;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
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

        SurveyResponseDTO surveyResponseDTO = (SurveyResponseDTO) o;
        if (surveyResponseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), surveyResponseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SurveyResponseDTO{" +
            "id=" + getId() +
            ", updated='" + getUpdated() + "'" +
            ", startedat='" + getStartedat() + "'" +
            ", completedat='" + getCompletedat() + "'" +
            ", survey=" + getSurveyId() +
            ", respondent=" + getRespondentId() +
            "}";
    }
}
