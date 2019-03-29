package com.vts.vee.web.rest;
import com.vts.vee.service.QuestionTypeService;
import com.vts.vee.web.rest.errors.BadRequestAlertException;
import com.vts.vee.web.rest.util.HeaderUtil;
import com.vts.vee.service.dto.QuestionTypeDTO;
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
 * REST controller for managing QuestionType.
 */
@RestController
@RequestMapping("/api")
public class QuestionTypeResource {

    private final Logger log = LoggerFactory.getLogger(QuestionTypeResource.class);

    private static final String ENTITY_NAME = "questionType";

    private final QuestionTypeService questionTypeService;

    public QuestionTypeResource(QuestionTypeService questionTypeService) {
        this.questionTypeService = questionTypeService;
    }

    /**
     * POST  /question-types : Create a new questionType.
     *
     * @param questionTypeDTO the questionTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questionTypeDTO, or with status 400 (Bad Request) if the questionType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/question-types")
    public ResponseEntity<QuestionTypeDTO> createQuestionType(@RequestBody QuestionTypeDTO questionTypeDTO) throws URISyntaxException {
        log.debug("REST request to save QuestionType : {}", questionTypeDTO);
        if (questionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionTypeDTO result = questionTypeService.save(questionTypeDTO);
        return ResponseEntity.created(new URI("/api/question-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /question-types : Updates an existing questionType.
     *
     * @param questionTypeDTO the questionTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questionTypeDTO,
     * or with status 400 (Bad Request) if the questionTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the questionTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/question-types")
    public ResponseEntity<QuestionTypeDTO> updateQuestionType(@RequestBody QuestionTypeDTO questionTypeDTO) throws URISyntaxException {
        log.debug("REST request to update QuestionType : {}", questionTypeDTO);
        if (questionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuestionTypeDTO result = questionTypeService.save(questionTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questionTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /question-types : get all the questionTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of questionTypes in body
     */
    @GetMapping("/question-types")
    public List<QuestionTypeDTO> getAllQuestionTypes() {
        log.debug("REST request to get all QuestionTypes");
        return questionTypeService.findAll();
    }

    /**
     * GET  /question-types/:id : get the "id" questionType.
     *
     * @param id the id of the questionTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questionTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/question-types/{id}")
    public ResponseEntity<QuestionTypeDTO> getQuestionType(@PathVariable Long id) {
        log.debug("REST request to get QuestionType : {}", id);
        Optional<QuestionTypeDTO> questionTypeDTO = questionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionTypeDTO);
    }

    /**
     * DELETE  /question-types/:id : delete the "id" questionType.
     *
     * @param id the id of the questionTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/question-types/{id}")
    public ResponseEntity<Void> deleteQuestionType(@PathVariable Long id) {
        log.debug("REST request to delete QuestionType : {}", id);
        questionTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/question-types?query=:query : search for the questionType corresponding
     * to the query.
     *
     * @param query the query of the questionType search
     * @return the result of the search
     */
    @GetMapping("/_search/question-types")
    public List<QuestionTypeDTO> searchQuestionTypes(@RequestParam String query) {
        log.debug("REST request to search QuestionTypes for query {}", query);
        return questionTypeService.search(query);
    }

}
