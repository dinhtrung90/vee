package com.vts.vee.service.impl;

import com.vts.vee.service.SurveyResponseService;
import com.vts.vee.domain.SurveyResponse;
import com.vts.vee.repository.SurveyResponseRepository;
import com.vts.vee.repository.search.SurveyResponseSearchRepository;
import com.vts.vee.service.dto.SurveyResponseDTO;
import com.vts.vee.service.mapper.SurveyResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SurveyResponse.
 */
@Service
@Transactional
public class SurveyResponseServiceImpl implements SurveyResponseService {

    private final Logger log = LoggerFactory.getLogger(SurveyResponseServiceImpl.class);

    private final SurveyResponseRepository surveyResponseRepository;

    private final SurveyResponseMapper surveyResponseMapper;

    private final SurveyResponseSearchRepository surveyResponseSearchRepository;

    public SurveyResponseServiceImpl(SurveyResponseRepository surveyResponseRepository, SurveyResponseMapper surveyResponseMapper, SurveyResponseSearchRepository surveyResponseSearchRepository) {
        this.surveyResponseRepository = surveyResponseRepository;
        this.surveyResponseMapper = surveyResponseMapper;
        this.surveyResponseSearchRepository = surveyResponseSearchRepository;
    }

    /**
     * Save a surveyResponse.
     *
     * @param surveyResponseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SurveyResponseDTO save(SurveyResponseDTO surveyResponseDTO) {
        log.debug("Request to save SurveyResponse : {}", surveyResponseDTO);
        SurveyResponse surveyResponse = surveyResponseMapper.toEntity(surveyResponseDTO);
        surveyResponse = surveyResponseRepository.save(surveyResponse);
        SurveyResponseDTO result = surveyResponseMapper.toDto(surveyResponse);
        surveyResponseSearchRepository.save(surveyResponse);
        return result;
    }

    /**
     * Get all the surveyResponses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SurveyResponseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SurveyResponses");
        return surveyResponseRepository.findAll(pageable)
            .map(surveyResponseMapper::toDto);
    }


    /**
     * Get one surveyResponse by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SurveyResponseDTO> findOne(Long id) {
        log.debug("Request to get SurveyResponse : {}", id);
        return surveyResponseRepository.findById(id)
            .map(surveyResponseMapper::toDto);
    }

    /**
     * Delete the surveyResponse by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SurveyResponse : {}", id);
        surveyResponseRepository.deleteById(id);
        surveyResponseSearchRepository.deleteById(id);
    }

    /**
     * Search for the surveyResponse corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SurveyResponseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SurveyResponses for query {}", query);
        return surveyResponseSearchRepository.search(queryStringQuery(query), pageable)
            .map(surveyResponseMapper::toDto);
    }
}
