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

import com.vts.vee.domain.enumeration.Gender;

/**
 * A Respondent.
 */
@Entity
@Table(name = "respondent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "respondent")
public class Respondent implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "email")
    private String email;

    @Column(name = "birth_day")
    private Instant birthDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "respondent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<VeeResponse> res = new HashSet<>();
    @OneToMany(mappedBy = "respondent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SurveyResponse> surveyResponses = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Respondent avatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public Respondent email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public Respondent birthDay(Instant birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public Gender getGender() {
        return gender;
    }

    public Respondent gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public User getUser() {
        return user;
    }

    public Respondent user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<VeeResponse> getRes() {
        return res;
    }

    public Respondent res(Set<VeeResponse> veeResponses) {
        this.res = veeResponses;
        return this;
    }

    public Respondent addRes(VeeResponse veeResponse) {
        this.res.add(veeResponse);
        veeResponse.setRespondent(this);
        return this;
    }

    public Respondent removeRes(VeeResponse veeResponse) {
        this.res.remove(veeResponse);
        veeResponse.setRespondent(null);
        return this;
    }

    public void setRes(Set<VeeResponse> veeResponses) {
        this.res = veeResponses;
    }

    public Set<SurveyResponse> getSurveyResponses() {
        return surveyResponses;
    }

    public Respondent surveyResponses(Set<SurveyResponse> surveyResponses) {
        this.surveyResponses = surveyResponses;
        return this;
    }

    public Respondent addSurveyResponse(SurveyResponse surveyResponse) {
        this.surveyResponses.add(surveyResponse);
        surveyResponse.setRespondent(this);
        return this;
    }

    public Respondent removeSurveyResponse(SurveyResponse surveyResponse) {
        this.surveyResponses.remove(surveyResponse);
        surveyResponse.setRespondent(null);
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
        Respondent respondent = (Respondent) o;
        if (respondent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), respondent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Respondent{" +
            "id=" + getId() +
            ", avatarUrl='" + getAvatarUrl() + "'" +
            ", email='" + getEmail() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
