package com.vts.vee.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A QuestionOrder.
 */
@Entity
@Table(name = "question_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "questionorder")
public class QuestionOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_order")
    private Integer order;

    @ManyToOne
    @JsonIgnoreProperties("questionOrders")
    private Survey survey;

    @ManyToOne
    @JsonIgnoreProperties("questionOrders")
    private Question questionOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public QuestionOrder order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Survey getSurvey() {
        return survey;
    }

    public QuestionOrder survey(Survey survey) {
        this.survey = survey;
        return this;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Question getQuestionOrder() {
        return questionOrder;
    }

    public QuestionOrder questionOrder(Question question) {
        this.questionOrder = question;
        return this;
    }

    public void setQuestionOrder(Question question) {
        this.questionOrder = question;
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
        QuestionOrder questionOrder = (QuestionOrder) o;
        if (questionOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), questionOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuestionOrder{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            "}";
    }
}
