package com.vts.vee.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A VeeResponse.
 */
@Entity
@Table(name = "vee_response")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "veeresponse")
public class VeeResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer")
    private String answer;

    @ManyToOne
    @JsonIgnoreProperties("res")
    private Question question;

    @ManyToOne
    @JsonIgnoreProperties("res")
    private Respondent respondent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public VeeResponse answer(String answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public VeeResponse question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Respondent getRespondent() {
        return respondent;
    }

    public VeeResponse respondent(Respondent respondent) {
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
        VeeResponse veeResponse = (VeeResponse) o;
        if (veeResponse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), veeResponse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VeeResponse{" +
            "id=" + getId() +
            ", answer='" + getAnswer() + "'" +
            "}";
    }
}
