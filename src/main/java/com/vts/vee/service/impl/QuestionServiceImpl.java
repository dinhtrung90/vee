package com.vts.vee.service.impl;

import com.vts.vee.service.QuestionService;
import com.vts.vee.domain.Question;
import com.vts.vee.repository.QuestionRepository;
import com.vts.vee.repository.search.QuestionSearchRepository;
import com.vts.vee.service.dto.QuestionDTO;
import com.vts.vee.service.mapper.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Question.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    private final QuestionSearchRepository questionSearchRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper, QuestionSearchRepository questionSearchRepository) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.questionSearchRepository = questionSearchRepository;
    }

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        QuestionDTO result = questionMapper.toDto(question);
        questionSearchRepository.save(question);
        return result;
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable)
            .map(questionMapper::toDto);
    }


    /**
     * Get one question by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionDTO> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findById(id)
            .map(questionMapper::toDto);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
        questionSearchRepository.deleteById(id);
    }

    /**
     * Search for the question corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Questions for query {}", query);
        return questionSearchRepository.search(queryStringQuery(query), pageable)
            .map(questionMapper::toDto);
    }
}
