package com.vts.vee.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Survey entity.
 */
public class SurveyDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Instant updated;

    private Instant openingtime;

    private Instant closingtime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getOpeningtime() {
        return openingtime;
    }

    public void setOpeningtime(Instant openingtime) {
        this.openingtime = openingtime;
    }

    public Instant getClosingtime() {
        return closingtime;
    }

    public void setClosingtime(Instant closingtime) {
        this.closingtime = closingtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SurveyDTO surveyDTO = (SurveyDTO) o;
        if (surveyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), surveyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SurveyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", openingtime='" + getOpeningtime() + "'" +
            ", closingtime='" + getClosingtime() + "'" +
            "}";
    }
}
