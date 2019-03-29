package com.vts.vee.service.mapper;

import com.vts.vee.domain.*;
import com.vts.vee.service.dto.QuestionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Question and its DTO QuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {


    @Mapping(target = "questionTypes", ignore = true)
    @Mapping(target = "responseChoices", ignore = true)
    @Mapping(target = "res", ignore = true)
    Question toEntity(QuestionDTO questionDTO);

    default Question fromId(Long id) {
        if (id == null) {
            return null;
        }
        Question question = new Question();
        question.setId(id);
        return question;
    }
}
