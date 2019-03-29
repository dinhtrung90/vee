package com.vts.vee.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Survey.
 */
@Entity
@Table(name = "survey")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "survey")
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "openingtime")
    private Instant openingtime;

    @Column(name = "closingtime")
    private Instant closingtime;

    @OneToMany(mappedBy = "survey")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<QuestionOrder> questionOrders = new HashSet<>();
    @OneToMany(mappedBy = "survey")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SurveyResponse> surveyResponses = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Survey name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Survey description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdated() {
        return updated;
    }

    public Survey updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getOpeningtime() {
        return openingtime;
    }

    public Survey openingtime(Instant openingtime) {
        this.openingtime = openingtime;
        return this;
    }

    public void setOpeningtime(Instant openingtime) {
        this.openingtime = openingtime;
    }

    public Instant getClosingtime() {
        return closingtime;
    }

    public Survey closingtime(Instant closingtime) {
        this.closingtime = closingtime;
        return this;
    }

    public void setClosingtime(Instant closingtime) {
        this.closingtime = closingtime;
    }

    public Set<QuestionOrder> getQuestionOrders() {
        return questionOrders;
    }

    public Survey questionOrders(Set<QuestionOrder> questionOrders) {
        this.questionOrders = questionOrders;
        return this;
    }

    public Survey addQuestionOrder(QuestionOrder questionOrder) {
        this.questionOrders.add(questionOrder);
        questionOrder.setSurvey(this);
        return this;
    }

    public Survey removeQuestionOrder(QuestionOrder questionOrder) {
        this.questionOrders.remove(questionOrder);
        questionOrder.setSurvey(null);
        return this;
    }

    public void setQuestionOrders(Set<QuestionOrder> questionOrders) {
        this.questionOrders = questionOrders;
    }

    public Set<SurveyResponse> getSurveyResponses() {
        return surveyResponses;
    }

    public Survey surveyResponses(Set<SurveyResponse> surveyResponses) {
        this.surveyResponses = surveyResponses;
        return this;
    }

    public Survey addSurveyResponse(SurveyResponse surveyResponse) {
        this.surveyResponses.add(surveyResponse);
        surveyResponse.setSurvey(this);
        return this;
    }

    public Survey removeSurveyResponse(SurveyResponse surveyResponse) {
        this.surveyResponses.remove(surveyResponse);
        surveyResponse.setSurvey(null);
        return this;
    }

    public void setSurveyResponses(Set<SurveyResponse> surveyResponses) {
        this.surveyResponses = surveyResponses;
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
        Survey survey = (Survey) o;
        if (survey.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), survey.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Survey{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", openingtime='" + getOpeningtime() + "'" +
            ", closingtime='" + getClosingtime() + "'" +
            "}";
    }
}
