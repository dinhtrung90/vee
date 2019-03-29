package com.vts.vee.service.mapper;

import com.vts.vee.domain.*;
import com.vts.vee.service.dto.ResponseChoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ResponseChoice and its DTO ResponseChoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface ResponseChoiceMapper extends EntityMapper<ResponseChoiceDTO, ResponseChoice> {

    @Mapping(source = "question.id", target = "questionId")
    ResponseChoiceDTO toDto(ResponseChoice responseChoice);

    @Mapping(source = "questionId", target = "question")
    ResponseChoice toEntity(ResponseChoiceDTO responseChoiceDTO);

    default ResponseChoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResponseChoice responseChoice = new ResponseChoice();
        responseChoice.setId(id);
        return responseChoice;
    }
}
