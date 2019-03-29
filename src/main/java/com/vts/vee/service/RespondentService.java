package com.vts.vee.service;

import com.vts.vee.service.dto.RespondentDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Respondent.
 */
public interface RespondentService {

    /**
     * Save a respondent.
     *
     * @param respondentDTO the entity to save
     * @return the persisted entity
     */
    RespondentDTO save(RespondentDTO respondentDTO);

    /**
     * Get all the respondents.
     *
     * @return the list of entities
     */
    List<RespondentDTO> findAll();


    /**
     * Get the "id" respondent.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RespondentDTO> findOne(Long id);

    /**
     * Delete the "id" respondent.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the respondent corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RespondentDTO> search(String query);
}
