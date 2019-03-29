package com.vts.vee.service.mapper;

import com.vts.vee.domain.*;
import com.vts.vee.service.dto.RespondentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Respondent and its DTO RespondentDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RespondentMapper extends EntityMapper<RespondentDTO, Respondent> {

    @Mapping(source = "user.id", target = "userId")
    RespondentDTO toDto(Respondent respondent);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "res", ignore = true)
    @Mapping(target = "surveyResponses", ignore = true)
    Respondent toEntity(RespondentDTO respondentDTO);

    default Respondent fromId(Long id) {
        if (id == null) {
            return null;
        }
        Respondent respondent = new Respondent();
        respondent.setId(id);
        return respondent;
    }
}
