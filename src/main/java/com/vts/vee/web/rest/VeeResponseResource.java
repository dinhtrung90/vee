package com.vts.vee.web.rest;
import com.vts.vee.service.VeeResponseService;
import com.vts.vee.web.rest.errors.BadRequestAlertException;
import com.vts.vee.web.rest.util.HeaderUtil;
import com.vts.vee.service.dto.VeeResponseDTO;
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
 * REST controller for managing VeeResponse.
 */
@RestController
@RequestMapping("/api")
public class VeeResponseResource {

    private final Logger log = LoggerFactory.getLogger(VeeResponseResource.class);

    private static final String ENTITY_NAME = "veeResponse";

    private final VeeResponseService veeResponseService;

    public VeeResponseResource(VeeResponseService veeResponseService) {
        this.veeResponseService = veeResponseService;
    }

    /**
     * POST  /vee-responses : Create a new veeResponse.
     *
     * @param veeResponseDTO the veeResponseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new veeResponseDTO, or with status 400 (Bad Request) if the veeResponse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vee-responses")
    public ResponseEntity<VeeResponseDTO> createVeeResponse(@RequestBody VeeResponseDTO veeResponseDTO) throws URISyntaxException {
        log.debug("REST request to save VeeResponse : {}", veeResponseDTO);
        if (veeResponseDTO.getId() != null) {
            throw new BadRequestAlertException("A new veeResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VeeResponseDTO result = veeResponseService.save(veeResponseDTO);
        return ResponseEntity.created(new URI("/api/vee-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vee-responses : Updates an existing veeResponse.
     *
     * @param veeResponseDTO the veeResponseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated veeResponseDTO,
     * or with status 400 (Bad Request) if the veeResponseDTO is not valid,
     * or with status 500 (Internal Server Error) if the veeResponseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vee-responses")
    public ResponseEntity<VeeResponseDTO> updateVeeResponse(@RequestBody VeeResponseDTO veeResponseDTO) throws URISyntaxException {
        log.debug("REST request to update VeeResponse : {}", veeResponseDTO);
        if (veeResponseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VeeResponseDTO result = veeResponseService.save(veeResponseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, veeResponseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vee-responses : get all the veeResponses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of veeResponses in body
     */
    @GetMapping("/vee-responses")
    public List<VeeResponseDTO> getAllVeeResponses() {
        log.debug("REST request to get all VeeResponses");
        return veeResponseService.findAll();
    }

    /**
     * GET  /vee-responses/:id : get the "id" veeResponse.
     *
     * @param id the id of the veeResponseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the veeResponseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/vee-responses/{id}")
    public ResponseEntity<VeeResponseDTO> getVeeResponse(@PathVariable Long id) {
        log.debug("REST request to get VeeResponse : {}", id);
        Optional<VeeResponseDTO> veeResponseDTO = veeResponseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(veeResponseDTO);
    }

    /**
     * DELETE  /vee-responses/:id : delete the "id" veeResponse.
     *
     * @param id the id of the veeResponseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vee-responses/{id}")
    public ResponseEntity<Void> deleteVeeResponse(@PathVariable Long id) {
        log.debug("REST request to delete VeeResponse : {}", id);
        veeResponseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/vee-responses?query=:query : search for the veeResponse corresponding
     * to the query.
     *
     * @param query the query of the veeResponse search
     * @return the result of the search
     */
    @GetMapping("/_search/vee-responses")
    public List<VeeResponseDTO> searchVeeResponses(@RequestParam String query) {
        log.debug("REST request to search VeeResponses for query {}", query);
        return veeResponseService.search(query);
    }

}
