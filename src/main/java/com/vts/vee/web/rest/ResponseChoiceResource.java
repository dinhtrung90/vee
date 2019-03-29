package com.vts.vee.web.rest;
import com.vts.vee.service.ResponseChoiceService;
import com.vts.vee.web.rest.errors.BadRequestAlertException;
import com.vts.vee.web.rest.util.HeaderUtil;
import com.vts.vee.service.dto.ResponseChoiceDTO;
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
 * REST controller for managing ResponseChoice.
 */
@RestController
@RequestMapping("/api")
public class ResponseChoiceResource {

    private final Logger log = LoggerFactory.getLogger(ResponseChoiceResource.class);

    private static final String ENTITY_NAME = "responseChoice";

    private final ResponseChoiceService responseChoiceService;

    public ResponseChoiceResource(ResponseChoiceService responseChoiceService) {
        this.responseChoiceService = responseChoiceService;
    }

    /**
     * POST  /response-choices : Create a new responseChoice.
     *
     * @param responseChoiceDTO the responseChoiceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new responseChoiceDTO, or with status 400 (Bad Request) if the responseChoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/response-choices")
    public ResponseEntity<ResponseChoiceDTO> createResponseChoice(@RequestBody ResponseChoiceDTO responseChoiceDTO) throws URISyntaxException {
        log.debug("REST request to save ResponseChoice : {}", responseChoiceDTO);
        if (responseChoiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new responseChoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResponseChoiceDTO result = responseChoiceService.save(responseChoiceDTO);
        return ResponseEntity.created(new URI("/api/response-choices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /response-choices : Updates an existing responseChoice.
     *
     * @param responseChoiceDTO the responseChoiceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated responseChoiceDTO,
     * or with status 400 (Bad Request) if the responseChoiceDTO is not valid,
     * or with status 500 (Internal Server Error) if the responseChoiceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/response-choices")
    public ResponseEntity<ResponseChoiceDTO> updateResponseChoice(@RequestBody ResponseChoiceDTO responseChoiceDTO) throws URISyntaxException {
        log.debug("REST request to update ResponseChoice : {}", responseChoiceDTO);
        if (responseChoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResponseChoiceDTO result = responseChoiceService.save(responseChoiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, responseChoiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /response-choices : get all the responseChoices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of responseChoices in body
     */
    @GetMapping("/response-choices")
    public List<ResponseChoiceDTO> getAllResponseChoices() {
        log.debug("REST request to get all ResponseChoices");
        return responseChoiceService.findAll();
    }

    /**
     * GET  /response-choices/:id : get the "id" responseChoice.
     *
     * @param id the id of the responseChoiceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the responseChoiceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/response-choices/{id}")
    public ResponseEntity<ResponseChoiceDTO> getResponseChoice(@PathVariable Long id) {
        log.debug("REST request to get ResponseChoice : {}", id);
        Optional<ResponseChoiceDTO> responseChoiceDTO = responseChoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(responseChoiceDTO);
    }

    /**
     * DELETE  /response-choices/:id : delete the "id" responseChoice.
     *
     * @param id the id of the responseChoiceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/response-choices/{id}")
    public ResponseEntity<Void> deleteResponseChoice(@PathVariable Long id) {
        log.debug("REST request to delete ResponseChoice : {}", id);
        responseChoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/response-choices?query=:query : search for the responseChoice corresponding
     * to the query.
     *
     * @param query the query of the responseChoice search
     * @return the result of the search
     */
    @GetMapping("/_search/response-choices")
    public List<ResponseChoiceDTO> searchResponseChoices(@RequestParam String query) {
        log.debug("REST request to search ResponseChoices for query {}", query);
        return responseChoiceService.search(query);
    }

}
