package com.vts.vee.web.rest;
import com.vts.vee.domain.QuestionOrder;
import com.vts.vee.service.QuestionOrderService;
import com.vts.vee.web.rest.errors.BadRequestAlertException;
import com.vts.vee.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing QuestionOrder.
 */
@RestController
@RequestMapping("/api")
public class QuestionOrderResource {

    private final Logger log = LoggerFactory.getLogger(QuestionOrderResource.class);

    private static final String ENTITY_NAME = "questionOrder";

    private final QuestionOrderService questionOrderService;

    public QuestionOrderResource(QuestionOrderService questionOrderService) {
        this.questionOrderService = questionOrderService;
    }

    /**
     * POST  /question-orders : Create a new questionOrder.
     *
     * @param questionOrder the questionOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questionOrder, or with status 400 (Bad Request) if the questionOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/question-orders")
    public ResponseEntity<QuestionOrder> createQuestionOrder(@RequestBody QuestionOrder questionOrder) throws URISyntaxException {
        log.debug("REST request to save QuestionOrder : {}", questionOrder);
        if (questionOrder.getId() != null) {
            throw new BadRequestAlertException("A new questionOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionOrder result = questionOrderService.save(questionOrder);
        return ResponseEntity.created(new URI("/api/question-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /question-orders : Updates an existing questionOrder.
     *
     * @param questionOrder the questionOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questionOrder,
     * or with status 400 (Bad Request) if the questionOrder is not valid,
     * or with status 500 (Internal Server Error) if the questionOrder couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/question-orders")
    public ResponseEntity<QuestionOrder> updateQuestionOrder(@RequestBody QuestionOrder questionOrder) throws URISyntaxException {
        log.debug("REST request to update QuestionOrder : {}", questionOrder);
        if (questionOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuestionOrder result = questionOrderService.save(questionOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questionOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /question-orders : get all the questionOrders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of questionOrders in body
     */
    @GetMapping("/question-orders")
    public List<QuestionOrder> getAllQuestionOrders() {
        log.debug("REST request to get all QuestionOrders");
        return questionOrderService.findAll();
    }

    /**
     * GET  /question-orders/:id : get the "id" questionOrder.
     *
     * @param id the id of the questionOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questionOrder, or with status 404 (Not Found)
     */
    @GetMapping("/question-orders/{id}")
    public ResponseEntity<QuestionOrder> getQuestionOrder(@PathVariable Long id) {
        log.debug("REST request to get QuestionOrder : {}", id);
        Optional<QuestionOrder> questionOrder = questionOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionOrder);
    }

    /**
     * DELETE  /question-orders/:id : delete the "id" questionOrder.
     *
     * @param id the id of the questionOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/question-orders/{id}")
    public ResponseEntity<Void> deleteQuestionOrder(@PathVariable Long id) {
        log.debug("REST request to delete QuestionOrder : {}", id);
        questionOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/question-orders?query=:query : search for the questionOrder corresponding
     * to the query.
     *
     * @param query the query of the questionOrder search
     * @return the result of the search
     */
    @GetMapping("/_search/question-orders")
    public List<QuestionOrder> searchQuestionOrders(@RequestParam String query) {
        log.debug("REST request to search QuestionOrders for query {}", query);
        return questionOrderService.search(query);
    }

}
