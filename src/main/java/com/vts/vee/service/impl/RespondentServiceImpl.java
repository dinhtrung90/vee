package com.vts.vee.service.impl;

import com.vts.vee.service.RespondentService;
import com.vts.vee.domain.Respondent;
import com.vts.vee.repository.RespondentRepository;
import com.vts.vee.repository.search.RespondentSearchRepository;
import com.vts.vee.service.dto.RespondentDTO;
import com.vts.vee.service.mapper.RespondentMapper;
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
 * Service Implementation for managing Respondent.
 */
@Service
@Transactional
public class RespondentServiceImpl implements RespondentService {

    private final Logger log = LoggerFactory.getLogger(RespondentServiceImpl.class);

    private final RespondentRepository respondentRepository;

    private final RespondentMapper respondentMapper;

    private final RespondentSearchRepository respondentSearchRepository;

    public RespondentServiceImpl(RespondentRepository respondentRepository, RespondentMapper respondentMapper, RespondentSearchRepository respondentSearchRepository) {
        this.respondentRepository = respondentRepository;
        this.respondentMapper = respondentMapper;
        this.respondentSearchRepository = respondentSearchRepository;
    }

    /**
     * Save a respondent.
     *
     * @param respondentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RespondentDTO save(RespondentDTO respondentDTO) {
        log.debug("Request to save Respondent : {}", respondentDTO);
        Respondent respondent = respondentMapper.toEntity(respondentDTO);
        respondent = respondentRepository.save(respondent);
        RespondentDTO result = respondentMapper.toDto(respondent);
        respondentSearchRepository.save(respondent);
        return result;
    }

    /**
     * Get all the respondents.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RespondentDTO> findAll() {
        log.debug("Request to get all Respondents");
        return respondentRepository.findAll().stream()
            .map(respondentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one respondent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RespondentDTO> findOne(Long id) {
        log.debug("Request to get Respondent : {}", id);
        return respondentRepository.findById(id)
            .map(respondentMapper::toDto);
    }

    /**
     * Delete the respondent by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Respondent : {}", id);
        respondentRepository.deleteById(id);
        respondentSearchRepository.deleteById(id);
    }

    /**
     * Search for the respondent corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RespondentDTO> search(String query) {
        log.debug("Request to search Respondents for query {}", query);
        return StreamSupport
            .stream(respondentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(respondentMapper::toDto)
            .collect(Collectors.toList());
    }
}
