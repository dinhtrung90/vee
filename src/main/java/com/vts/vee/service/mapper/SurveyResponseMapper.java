package com.vts.vee.service.mapper;

import com.vts.vee.domain.*;
import com.vts.vee.service.dto.SurveyResponseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SurveyResponse and its DTO SurveyResponseDTO.
 */
@Mapper(componentModel = "spring", uses = {SurveyMapper.class, RespondentMapper.class})
public interface SurveyResponseMapper extends EntityMapper<SurveyResponseDTO, SurveyResponse> {

    @Mapping(source = "survey.id", target = "surveyId")
    @Mapping(source = "respondent.id", target = "respondentId")
    SurveyResponseDTO toDto(SurveyResponse surveyResponse);

    @Mapping(source = "surveyId", target = "survey")
    @Mapping(source = "respondentId", target = "respondent")
    SurveyResponse toEntity(SurveyResponseDTO surveyResponseDTO);

    default SurveyResponse fromId(Long id) {
        if (id == null) {
            return null;
        }
        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setId(id);
        return surveyResponse;
    }
}
