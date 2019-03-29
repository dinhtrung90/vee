package com.vts.vee.web.rest;
import com.vts.vee.service.RespondentService;
import com.vts.vee.web.rest.errors.BadRequestAlertException;
import com.vts.vee.web.rest.util.HeaderUtil;
import com.vts.vee.service.dto.RespondentDTO;
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
 * REST controller for managing Respondent.
 */
@RestController
@RequestMapping("/api")
public class RespondentResource {

    private final Logger log = LoggerFactory.getLogger(RespondentResource.class);

    private static final String ENTITY_NAME = "respondent";

    private final RespondentService respondentService;

    public RespondentResource(RespondentService respondentService) {
        this.respondentService = respondentService;
    }

    /**
     * POST  /respondents : Create a new respondent.
     *
     * @param respondentDTO the respondentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new respondentDTO, or with status 400 (Bad Request) if the respondent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/respondents")
    public ResponseEntity<RespondentDTO> createRespondent(@RequestBody RespondentDTO respondentDTO) throws URISyntaxException {
        log.debug("REST request to save Respondent : {}", respondentDTO);
        if (respondentDTO.getId() != null) {
            throw new BadRequestAlertException("A new respondent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RespondentDTO result = respondentService.save(respondentDTO);
        return ResponseEntity.created(new URI("/api/respondents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /respondents : Updates an existing respondent.
     *
     * @param respondentDTO the respondentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated respondentDTO,
     * or with status 400 (Bad Request) if the respondentDTO is not valid,
     * or with status 500 (Internal Server Error) if the respondentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/respondents")
    public ResponseEntity<RespondentDTO> updateRespondent(@RequestBody RespondentDTO respondentDTO) throws URISyntaxException {
        log.debug("REST request to update Respondent : {}", respondentDTO);
        if (respondentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RespondentDTO result = respondentService.save(respondentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, respondentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /respondents : get all the respondents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of respondents in body
     */
    @GetMapping("/respondents")
    public List<RespondentDTO> getAllRespondents() {
        log.debug("REST request to get all Respondents");
        return respondentService.findAll();
    }

    /**
     * GET  /respondents/:id : get the "id" respondent.
     *
     * @param id the id of the respondentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the respondentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/respondents/{id}")
    public ResponseEntity<RespondentDTO> getRespondent(@PathVariable Long id) {
        log.debug("REST request to get Respondent : {}", id);
        Optional<RespondentDTO> respondentDTO = respondentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(respondentDTO);
    }

    /**
     * DELETE  /respondents/:id : delete the "id" respondent.
     *
     * @param id the id of the respondentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/respondents/{id}")
    public ResponseEntity<Void> deleteRespondent(@PathVariable Long id) {
        log.debug("REST request to delete Respondent : {}", id);
        respondentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/respondents?query=:query : search for the respondent corresponding
     * to the query.
     *
     * @param query the query of the respondent search
     * @return the result of the search
     */
    @GetMapping("/_search/respondents")
    public List<RespondentDTO> searchRespondents(@RequestParam String query) {
        log.debug("REST request to search Respondents for query {}", query);
        return respondentService.search(query);
    }

}
