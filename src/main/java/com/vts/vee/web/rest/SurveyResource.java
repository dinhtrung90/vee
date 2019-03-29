package com.vts.vee.web.rest;
import com.vts.vee.service.SurveyService;
import com.vts.vee.web.rest.errors.BadRequestAlertException;
import com.vts.vee.web.rest.util.HeaderUtil;
import com.vts.vee.web.rest.util.PaginationUtil;
import com.vts.vee.service.dto.SurveyDTO;
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
 * REST controller for managing Survey.
 */
@RestController
@RequestMapping("/api")
public class SurveyResource {

    private final Logger log = LoggerFactory.getLogger(SurveyResource.class);

    private static final String ENTITY_NAME = "survey";

    private final SurveyService surveyService;

    public SurveyResource(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /**
     * POST  /surveys : Create a new survey.
     *
     * @param surveyDTO the surveyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new surveyDTO, or with status 400 (Bad Request) if the survey has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/surveys")
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO surveyDTO) throws URISyntaxException {
        log.debug("REST request to save Survey : {}", surveyDTO);
        if (surveyDTO.getId() != null) {
            throw new BadRequestAlertException("A new survey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SurveyDTO result = surveyService.save(surveyDTO);
        return ResponseEntity.created(new URI("/api/surveys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /surveys : Updates an existing survey.
     *
     * @param surveyDTO the surveyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated surveyDTO,
     * or with status 400 (Bad Request) if the surveyDTO is not valid,
     * or with status 500 (Internal Server Error) if the surveyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/surveys")
    public ResponseEntity<SurveyDTO> updateSurvey(@RequestBody SurveyDTO surveyDTO) throws URISyntaxException {
        log.debug("REST request to update Survey : {}", surveyDTO);
        if (surveyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SurveyDTO result = surveyService.save(surveyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, surveyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /surveys : get all the surveys.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of surveys in body
     */
    @GetMapping("/surveys")
    public ResponseEntity<List<SurveyDTO>> getAllSurveys(Pageable pageable) {
        log.debug("REST request to get a page of Surveys");
        Page<SurveyDTO> page = surveyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/surveys");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /surveys/:id : get the "id" survey.
     *
     * @param id the id of the surveyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the surveyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/surveys/{id}")
    public ResponseEntity<SurveyDTO> getSurvey(@PathVariable Long id) {
        log.debug("REST request to get Survey : {}", id);
        Optional<SurveyDTO> surveyDTO = surveyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(surveyDTO);
    }

    /**
     * DELETE  /surveys/:id : delete the "id" survey.
     *
     * @param id the id of the surveyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/surveys/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        log.debug("REST request to delete Survey : {}", id);
        surveyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/surveys?query=:query : search for the survey corresponding
     * to the query.
     *
     * @param query the query of the survey search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/surveys")
    public ResponseEntity<List<SurveyDTO>> searchSurveys(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Surveys for query {}", query);
        Page<SurveyDTO> page = surveyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/surveys");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
