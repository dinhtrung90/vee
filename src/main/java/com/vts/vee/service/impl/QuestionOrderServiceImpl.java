package com.vts.vee.service.impl;

import com.vts.vee.service.QuestionOrderService;
import com.vts.vee.domain.QuestionOrder;
import com.vts.vee.repository.QuestionOrderRepository;
import com.vts.vee.repository.search.QuestionOrderSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing QuestionOrder.
 */
@Service
@Transactional
public class QuestionOrderServiceImpl implements QuestionOrderService {

    private final Logger log = LoggerFactory.getLogger(QuestionOrderServiceImpl.class);

    private final QuestionOrderRepository questionOrderRepository;

    private final QuestionOrderSearchRepository questionOrderSearchRepository;

    public QuestionOrderServiceImpl(QuestionOrderRepository questionOrderRepository, QuestionOrderSearchRepository questionOrderSearchRepository) {
        this.questionOrderRepository = questionOrderRepository;
        this.questionOrderSearchRepository = questionOrderSearchRepository;
    }

    /**
     * Save a questionOrder.
     *
     * @param questionOrder the entity to save
     * @return the persisted entity
     */
    @Override
    public QuestionOrder save(QuestionOrder questionOrder) {
        log.debug("Request to save QuestionOrder : {}", questionOrder);
        QuestionOrder result = questionOrderRepository.save(questionOrder);
        questionOrderSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the questionOrders.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<QuestionOrder> findAll() {
        log.debug("Request to get all QuestionOrders");
        return questionOrderRepository.findAll();
    }


    /**
     * Get one questionOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionOrder> findOne(Long id) {
        log.debug("Request to get QuestionOrder : {}", id);
        return questionOrderRepository.findById(id);
    }

    /**
     * Delete the questionOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuestionOrder : {}", id);
        questionOrderRepository.deleteById(id);
        questionOrderSearchRepository.deleteById(id);
    }

    /**
     * Search for the questionOrder corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<QuestionOrder> search(String query) {
        log.debug("Request to search QuestionOrders for query {}", query);
        return StreamSupport
            .stream(questionOrderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
