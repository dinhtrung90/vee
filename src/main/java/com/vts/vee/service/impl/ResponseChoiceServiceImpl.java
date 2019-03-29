package com.vts.vee.service.impl;

import com.vts.vee.service.ResponseChoiceService;
import com.vts.vee.domain.ResponseChoice;
import com.vts.vee.repository.ResponseChoiceRepository;
import com.vts.vee.repository.search.ResponseChoiceSearchRepository;
import com.vts.vee.service.dto.ResponseChoiceDTO;
import com.vts.vee.service.mapper.ResponseChoiceMapper;
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
 * Service Implementation for managing ResponseChoice.
 */
@Service
@Transactional
public class ResponseChoiceServiceImpl implements ResponseChoiceService {

    private final Logger log = LoggerFactory.getLogger(ResponseChoiceServiceImpl.class);

    private final ResponseChoiceRepository responseChoiceRepository;

    private final ResponseChoiceMapper responseChoiceMapper;

    private final ResponseChoiceSearchRepository responseChoiceSearchRepository;

    public ResponseChoiceServiceImpl(ResponseChoiceRepository responseChoiceRepository, ResponseChoiceMapper responseChoiceMapper, ResponseChoiceSearchRepository responseChoiceSearchRepository) {
        this.responseChoiceRepository = responseChoiceRepository;
        this.responseChoiceMapper = responseChoiceMapper;
        this.responseChoiceSearchRepository = responseChoiceSearchRepository;
    }

    /**
     * Save a responseChoice.
     *
     * @param responseChoiceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ResponseChoiceDTO save(ResponseChoiceDTO responseChoiceDTO) {
        log.debug("Request to save ResponseChoice : {}", responseChoiceDTO);
        ResponseChoice responseChoice = responseChoiceMapper.toEntity(responseChoiceDTO);
        responseChoice = responseChoiceRepository.save(responseChoice);
        ResponseChoiceDTO result = responseChoiceMapper.toDto(responseChoice);
        responseChoiceSearchRepository.save(responseChoice);
        return result;
    }

    /**
     * Get all the responseChoices.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseChoiceDTO> findAll() {
        log.debug("Request to get all ResponseChoices");
        return responseChoiceRepository.findAll().stream()
            .map(responseChoiceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one responseChoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ResponseChoiceDTO> findOne(Long id) {
        log.debug("Request to get ResponseChoice : {}", id);
        return responseChoiceRepository.findById(id)
            .map(responseChoiceMapper::toDto);
    }

    /**
     * Delete the responseChoice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ResponseChoice : {}", id);
        responseChoiceRepository.deleteById(id);
        responseChoiceSearchRepository.deleteById(id);
    }

    /**
     * Search for the responseChoice corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseChoiceDTO> search(String query) {
        log.debug("Request to search ResponseChoices for query {}", query);
        return StreamSupport
            .stream(responseChoiceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(responseChoiceMapper::toDto)
            .collect(Collectors.toList());
    }
}
