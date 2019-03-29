package com.vts.vee.service.mapper;

import com.vts.vee.domain.*;
import com.vts.vee.service.dto.VeeResponseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity VeeResponse and its DTO VeeResponseDTO.
 */
@Mapper(componentModel = "spring", uses = {QuestionMapper.class, RespondentMapper.class})
public interface VeeResponseMapper extends EntityMapper<VeeResponseDTO, VeeResponse> {

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "respondent.id", target = "respondentId")
    VeeResponseDTO toDto(VeeResponse veeResponse);

    @Mapping(source = "questionId", target = "question")
    @Mapping(source = "respondentId", target = "respondent")
    VeeResponse toEntity(VeeResponseDTO veeResponseDTO);

    default VeeResponse fromId(Long id) {
        if (id == null) {
            return null;
        }
        VeeResponse veeResponse = new VeeResponse();
        veeResponse.setId(id);
        return veeResponse;
    }
}
