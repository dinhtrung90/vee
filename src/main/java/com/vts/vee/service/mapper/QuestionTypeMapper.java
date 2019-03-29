package com.vts.vee.service.mapper;

import com.vts.vee.domain.*;
import com.vts.vee.service.dto.QuestionTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity QuestionType and its DTO QuestionTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface QuestionTypeMapper extends EntityMapper<QuestionTypeDTO, QuestionType> {

    @Mapping(source = "question.id", target = "questionId")
    QuestionTypeDTO toDto(QuestionType questionType);

    @Mapping(source = "questionId", target = "question")
    QuestionType toEntity(QuestionTypeDTO questionTypeDTO);

    default QuestionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuestionType questionType = new QuestionType();
        questionType.setId(id);
        return questionType;
    }
}
