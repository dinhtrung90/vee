package com.vts.vee.service.impl;

import com.vts.vee.service.VeeResponseService;
import com.vts.vee.domain.VeeResponse;
import com.vts.vee.repository.VeeResponseRepository;
import com.vts.vee.repository.search.VeeResponseSearchRepository;
import com.vts.vee.service.dto.VeeResponseDTO;
import com.vts.vee.service.mapper.VeeResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing VeeResponse.
 */
@Service
@Transactional
public class VeeResponseServiceImpl implements VeeResponseService {

    private final Logger log = LoggerFactory.getLogger(VeeResponseServiceImpl.class);

    private final VeeResponseRepository veeResponseRepository;

    private final VeeResponseMapper veeResponseMapper;

    private final VeeResponseSearchRepository veeResponseSearchRepository;

    public VeeResponseServiceImpl(VeeResponseRepository veeResponseRepository, VeeResponseMapper veeResponseMapper, VeeResponseSearchRepository veeResponseSearchRepository) {
        this.veeResponseRepository = veeResponseRepository;
        this.veeResponseMapper = veeResponseMapper;
        this.veeResponseSearchRepository = veeResponseSearchRepository;
    }

    /**
     * Save a veeResponse.
     *
     * @param veeResponseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VeeResponseDTO save(VeeResponseDTO veeResponseDTO) {
        log.debug("Request to save VeeResponse : {}", veeResponseDTO);
        VeeResponse veeResponse = veeResponseMapper.toEntity(veeResponseDTO);
        veeResponse = veeResponseRepository.save(veeResponse);
        VeeResponseDTO result = veeResponseMapper.toDto(veeResponse);
        veeResponseSearchRepository.save(veeResponse);
        return result;
    }

    /**
     * Get all the veeResponses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<VeeResponseDTO> findAll() {
        log.debug("Request to get all VeeResponses");
        return veeResponseRepository.findAll().stream()
            .map(veeResponseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one veeResponse by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VeeResponseDTO> findOne(Long id) {
        log.debug("Request to get VeeResponse : {}", id);
        return veeResponseRepository.findById(id)
            .map(veeResponseMapper::toDto);
    }

    /**
     * Delete the veeResponse by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VeeResponse : {}", id);
        veeResponseRepository.deleteById(id);
        veeResponseSearchRepository.deleteById(id);
    }

    /**
     * Search for the veeResponse corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<VeeResponseDTO> search(String query) {
        log.debug("Request to search VeeResponses for query {}", query);
        return StreamSupport
            .stream(veeResponseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(veeResponseMapper::toDto)
            .collect(Collectors.toList());
    }
}
