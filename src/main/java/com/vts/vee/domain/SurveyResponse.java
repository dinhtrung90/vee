package com.vts.vee.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A SurveyResponse.
 */
@Entity
@Table(name = "survey_response")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "surveyresponse")
public class SurveyResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "startedat")
    private Instant startedat;

    @Column(name = "completedat")
    private Instant completedat;

    @ManyToOne
    @JsonIgnoreProperties("surveyResponses")
    private Survey survey;

    @ManyToOne
    @JsonIgnoreProperties("surveyResponses")
    private Respondent respondent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUpdated() {
        return updated;
    }

    public SurveyResponse updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getStartedat() {
        return startedat;
    }

    public SurveyResponse startedat(Instant startedat) {
        this.startedat = startedat;
        return this;
    }

    public void setStartedat(Instant startedat) {
        this.startedat = startedat;
    }

    public Instant getCompletedat() {
        return completedat;
    }

    public SurveyResponse completedat(Instant completedat) {
        this.completedat = completedat;
        return this;
    }

    public void setCompletedat(Instant completedat) {
        this.completedat = completedat;
    }

    public Survey getSurvey() {
        return survey;
    }

    public SurveyResponse survey(Survey survey) {
        this.survey = survey;
        return this;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Respondent getRespondent() {
        return respondent;
    }

    public SurveyResponse respondent(Respondent respondent) {
        this.respondent = respondent;
        return this;
    }

    public void setRespondent(Respondent respondent) {
        this.respondent = respondent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SurveyResponse surveyResponse = (SurveyResponse) o;
        if (surveyResponse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), surveyResponse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SurveyResponse{" +
            "id=" + getId() +
            ", updated='" + getUpdated() + "'" +
            ", startedat='" + getStartedat() + "'" +
            ", completedat='" + getCompletedat() + "'" +
            "}";
    }
}
