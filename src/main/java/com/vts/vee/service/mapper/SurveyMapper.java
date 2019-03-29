package com.vts.vee.service.mapper;

import com.vts.vee.domain.*;
import com.vts.vee.service.dto.SurveyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Survey and its DTO SurveyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SurveyMapper extends EntityMapper<SurveyDTO, Survey> {


    @Mapping(target = "questionOrders", ignore = true)
    @Mapping(target = "surveyResponses", ignore = true)
    Survey toEntity(SurveyDTO surveyDTO);

    default Survey fromId(Long id) {
        if (id == null) {
            return null;
        }
        Survey survey = new Survey();
        survey.setId(id);
        return survey;
    }
}
