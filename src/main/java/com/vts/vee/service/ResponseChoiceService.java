package com.vts.vee.service;

import com.vts.vee.service.dto.ResponseChoiceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ResponseChoice.
 */
public interface ResponseChoiceService {

    /**
     * Save a responseChoice.
     *
     * @param responseChoiceDTO the entity to save
     * @return the persisted entity
     */
    ResponseChoiceDTO save(ResponseChoiceDTO responseChoiceDTO);

    /**
     * Get all the responseChoices.
     *
     * @return the list of entities
     */
    List<ResponseChoiceDTO> findAll();


    /**
     * Get the "id" responseChoice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ResponseChoiceDTO> findOne(Long id);

    /**
     * Delete the "id" responseChoice.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the responseChoice corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ResponseChoiceDTO> search(String query);
}
