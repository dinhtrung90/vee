package com.vts.vee.web.rest;
import com.vts.vee.service.SurveyResponseService;
import com.vts.vee.web.rest.errors.BadRequestAlertException;
import com.vts.vee.web.rest.util.HeaderUtil;
import com.vts.vee.web.rest.util.PaginationUtil;
import com.vts.vee.service.dto.SurveyResponseDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SurveyResponse.
 */
@RestController
@RequestMapping("/api")
public class SurveyResponseResource {

    private final Logger log = LoggerFactory.getLogger(SurveyResponseResource.class);

    private static final String ENTITY_NAME = "surveyResponse";

    private final SurveyResponseService surveyResponseService;

    public SurveyResponseResource(SurveyResponseService surveyResponseService) {
        this.surveyResponseService = surveyResponseService;
    }

    /**
     * POST  /survey-responses : Create a new surveyResponse.
     *
     * @param surveyResponseDTO the surveyResponseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new surveyResponseDTO, or with status 400 (Bad Request) if the surveyResponse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/survey-responses")
    public ResponseEntity<SurveyResponseDTO> createSurveyResponse(@RequestBody SurveyResponseDTO surveyResponseDTO) throws URISyntaxException {
        log.debug("REST request to save SurveyResponse : {}", surveyResponseDTO);
        if (surveyResponseDTO.getId() != null) {
            throw new BadRequestAlertException("A new surveyResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SurveyResponseDTO result = surveyResponseService.save(surveyResponseDTO);
        return ResponseEntity.created(new URI("/api/survey-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /survey-responses : Updates an existing surveyResponse.
     *
     * @param surveyResponseDTO the surveyResponseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated surveyResponseDTO,
     * or with status 400 (Bad Request) if the surveyResponseDTO is not valid,
     * or with status 500 (Internal Server Error) if the surveyResponseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/survey-responses")
    public ResponseEntity<SurveyResponseDTO> updateSurveyResponse(@RequestBody SurveyResponseDTO surveyResponseDTO) throws URISyntaxException {
        log.debug("REST request to update SurveyResponse : {}", surveyResponseDTO);
        if (surveyResponseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SurveyResponseDTO result = surveyResponseService.save(surveyResponseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, surveyResponseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /survey-responses : get all the surveyResponses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of surveyResponses in body
     */
    @GetMapping("/survey-responses")
    public ResponseEntity<List<SurveyResponseDTO>> getAllSurveyResponses(Pageable pageable) {
        log.debug("REST request to get a page of SurveyResponses");
        Page<SurveyResponseDTO> page = surveyResponseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/survey-responses");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /survey-responses/:id : get the "id" surveyResponse.
     *
     * @param id the id of the surveyResponseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the surveyResponseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/survey-responses/{id}")
    public ResponseEntity<SurveyResponseDTO> getSurveyResponse(@PathVariable Long id) {
        log.debug("REST request to get SurveyResponse : {}", id);
        Optional<SurveyResponseDTO> surveyResponseDTO = surveyResponseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(surveyResponseDTO);
    }

    /**
     * DELETE  /survey-responses/:id : delete the "id" surveyResponse.
     *
     * @param id the id of the surveyResponseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/survey-responses/{id}")
    public ResponseEntity<Void> deleteSurveyResponse(@PathVariable Long id) {
        log.debug("REST request to delete SurveyResponse : {}", id);
        surveyResponseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/survey-responses?query=:query : search for the surveyResponse corresponding
     * to the query.
     *
     * @param query the query of the surveyResponse search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/survey-responses")
    public ResponseEntity<List<SurveyResponseDTO>> searchSurveyResponses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SurveyResponses for query {}", query);
        Page<SurveyResponseDTO> page = surveyResponseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/survey-responses");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
