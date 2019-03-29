package com.vts.vee.service;

import com.vts.vee.domain.QuestionOrder;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing QuestionOrder.
 */
public interface QuestionOrderService {

    /**
     * Save a questionOrder.
     *
     * @param questionOrder the entity to save
     * @return the persisted entity
     */
    QuestionOrder save(QuestionOrder questionOrder);

    /**
     * Get all the questionOrders.
     *
     * @return the list of entities
     */
    List<QuestionOrder> findAll();


    /**
     * Get the "id" questionOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<QuestionOrder> findOne(Long id);

    /**
     * Delete the "id" questionOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the questionOrder corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<QuestionOrder> search(String query);
}
