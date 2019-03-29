package com.vts.vee.service.impl;

import com.vts.vee.service.SurveyService;
import com.vts.vee.domain.Survey;
import com.vts.vee.repository.SurveyRepository;
import com.vts.vee.repository.search.SurveySearchRepository;
import com.vts.vee.service.dto.SurveyDTO;
import com.vts.vee.service.mapper.SurveyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Survey.
 */
@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);

    private final SurveyRepository surveyRepository;

    private final SurveyMapper surveyMapper;

    private final SurveySearchRepository surveySearchRepository;

    public SurveyServiceImpl(SurveyRepository surveyRepository, SurveyMapper surveyMapper, SurveySearchRepository surveySearchRepository) {
        this.surveyRepository = surveyRepository;
        this.surveyMapper = surveyMapper;
        this.surveySearchRepository = surveySearchRepository;
    }

    /**
     * Save a survey.
     *
     * @param surveyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SurveyDTO save(SurveyDTO surveyDTO) {
        log.debug("Request to save Survey : {}", surveyDTO);
        Survey survey = surveyMapper.toEntity(surveyDTO);
        survey = surveyRepository.save(survey);
        SurveyDTO result = surveyMapper.toDto(survey);
        surveySearchRepository.save(survey);
        return result;
    }

    /**
     * Get all the surveys.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SurveyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Surveys");
        return surveyRepository.findAll(pageable)
            .map(surveyMapper::toDto);
    }


    /**
     * Get one survey by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SurveyDTO> findOne(Long id) {
        log.debug("Request to get Survey : {}", id);
        return surveyRepository.findById(id)
            .map(surveyMapper::toDto);
    }

    /**
     * Delete the survey by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Survey : {}", id);
        surveyRepository.deleteById(id);
        surveySearchRepository.deleteById(id);
    }

    /**
     * Search for the survey corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SurveyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Surveys for query {}", query);
        return surveySearchRepository.search(queryStringQuery(query), pageable)
            .map(surveyMapper::toDto);
    }
}
