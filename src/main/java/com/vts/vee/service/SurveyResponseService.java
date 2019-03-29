package com.vts.vee.service;

import com.vts.vee.service.dto.SurveyResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SurveyResponse.
 */
public interface SurveyResponseService {

    /**
     * Save a surveyResponse.
     *
     * @param surveyResponseDTO the entity to save
     * @return the persisted entity
     */
    SurveyResponseDTO save(SurveyResponseDTO surveyResponseDTO);

    /**
     * Get all the surveyResponses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SurveyResponseDTO> findAll(Pageable pageable);


    /**
     * Get the "id" surveyResponse.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SurveyResponseDTO> findOne(Long id);

    /**
     * Delete the "id" surveyResponse.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the surveyResponse corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SurveyResponseDTO> search(String query, Pageable pageable);
}
