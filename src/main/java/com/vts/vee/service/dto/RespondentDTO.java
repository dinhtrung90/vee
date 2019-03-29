package com.vts.vee.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import com.vts.vee.domain.enumeration.Gender;

/**
 * A DTO for the Respondent entity.
 */
public class RespondentDTO implements Serializable {

    private Long id;

    private String avatarUrl;

    private String email;

    private Instant birthDay;

    private Gender gender;


    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RespondentDTO respondentDTO = (RespondentDTO) o;
        if (respondentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), respondentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RespondentDTO{" +
            "id=" + getId() +
            ", avatarUrl='" + getAvatarUrl() + "'" +
            ", email='" + getEmail() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            ", gender='" + getGender() + "'" +
            ", user=" + getUserId() +
            "}";
    }
}
