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
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "updated")
    private Instant updated;

    @OneToMany(mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<QuestionType> questionTypes = new HashSet<>();
    @OneToMany(mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResponseChoice> responseChoices = new HashSet<>();
    @OneToMany(mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<VeeResponse> res = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Question text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getUpdated() {
        return updated;
    }

    public Question updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Set<QuestionType> getQuestionTypes() {
        return questionTypes;
    }

    public Question questionTypes(Set<QuestionType> questionTypes) {
        this.questionTypes = questionTypes;
        return this;
    }

    public Question addQuestionType(QuestionType questionType) {
        this.questionTypes.add(questionType);
        questionType.setQuestion(this);
        return this;
    }

    public Question removeQuestionType(QuestionType questionType) {
        this.questionTypes.remove(questionType);
        questionType.setQuestion(null);
        return this;
    }

    public void setQuestionTypes(Set<QuestionType> questionTypes) {
        this.questionTypes = questionTypes;
    }

    public Set<ResponseChoice> getResponseChoices() {
        return responseChoices;
    }

    public Question responseChoices(Set<ResponseChoice> responseChoices) {
        this.responseChoices = responseChoices;
        return this;
    }

    public Question addResponseChoice(ResponseChoice responseChoice) {
        this.responseChoices.add(responseChoice);
        responseChoice.setQuestion(this);
        return this;
    }

    public Question removeResponseChoice(ResponseChoice responseChoice) {
        this.responseChoices.remove(responseChoice);
        responseChoice.setQuestion(null);
        return this;
    }

    public void setResponseChoices(Set<ResponseChoice> responseChoices) {
        this.responseChoices = responseChoices;
    }

    public Set<VeeResponse> getRes() {
        return res;
    }

    public Question res(Set<VeeResponse> veeResponses) {
        this.res = veeResponses;
        return this;
    }

    public Question addRes(VeeResponse veeResponse) {
        this.res.add(veeResponse);
        veeResponse.setQuestion(this);
        return this;
    }

    public Question removeRes(VeeResponse veeResponse) {
        this.res.remove(veeResponse);
        veeResponse.setQuestion(null);
        return this;
    }

    public void setRes(Set<VeeResponse> veeResponses) {
        this.res = veeResponses;
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
        Question question = (Question) o;
        if (question.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
