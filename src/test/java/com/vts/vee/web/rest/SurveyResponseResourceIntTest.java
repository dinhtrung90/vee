package com.vts.vee.web.rest;

import com.vts.vee.VeeApp;

import com.vts.vee.domain.SurveyResponse;
import com.vts.vee.repository.SurveyResponseRepository;
import com.vts.vee.repository.search.SurveyResponseSearchRepository;
import com.vts.vee.service.SurveyResponseService;
import com.vts.vee.service.dto.SurveyResponseDTO;
import com.vts.vee.service.mapper.SurveyResponseMapper;
import com.vts.vee.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.vts.vee.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SurveyResponseResource REST controller.
 *
 * @see SurveyResponseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VeeApp.class)
public class SurveyResponseResourceIntTest {

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_STARTEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    @Autowired
    private SurveyResponseMapper surveyResponseMapper;

    @Autowired
    private SurveyResponseService surveyResponseService;

    /**
     * This repository is mocked in the com.vts.vee.repository.search test package.
     *
     * @see com.vts.vee.repository.search.SurveyResponseSearchRepositoryMockConfiguration
     */
    @Autowired
    private SurveyResponseSearchRepository mockSurveyResponseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSurveyResponseMockMvc;

    private SurveyResponse surveyResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SurveyResponseResource surveyResponseResource = new SurveyResponseResource(surveyResponseService);
        this.restSurveyResponseMockMvc = MockMvcBuilders.standaloneSetup(surveyResponseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurveyResponse createEntity(EntityManager em) {
        SurveyResponse surveyResponse = new SurveyResponse()
            .updated(DEFAULT_UPDATED)
            .startedat(DEFAULT_STARTEDAT)
            .completedat(DEFAULT_COMPLETEDAT);
        return surveyResponse;
    }

    @Before
    public void initTest() {
        surveyResponse = createEntity(em);
    }

    @Test
    @Transactional
    public void createSurveyResponse() throws Exception {
        int databaseSizeBeforeCreate = surveyResponseRepository.findAll().size();

        // Create the SurveyResponse
        SurveyResponseDTO surveyResponseDTO = surveyResponseMapper.toDto(surveyResponse);
        restSurveyResponseMockMvc.perform(post("/api/survey-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyResponseDTO)))
            .andExpect(status().isCreated());

        // Validate the SurveyResponse in the database
        List<SurveyResponse> surveyResponseList = surveyResponseRepository.findAll();
        assertThat(surveyResponseList).hasSize(databaseSizeBeforeCreate + 1);
        SurveyResponse testSurveyResponse = surveyResponseList.get(surveyResponseList.size() - 1);
        assertThat(testSurveyResponse.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testSurveyResponse.getStartedat()).isEqualTo(DEFAULT_STARTEDAT);
        assertThat(testSurveyResponse.getCompletedat()).isEqualTo(DEFAULT_COMPLETEDAT);

        // Validate the SurveyResponse in Elasticsearch
        verify(mockSurveyResponseSearchRepository, times(1)).save(testSurveyResponse);
    }

    @Test
    @Transactional
    public void createSurveyResponseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = surveyResponseRepository.findAll().size();

        // Create the SurveyResponse with an existing ID
        surveyResponse.setId(1L);
        SurveyResponseDTO surveyResponseDTO = surveyResponseMapper.toDto(surveyResponse);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveyResponseMockMvc.perform(post("/api/survey-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyResponseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SurveyResponse in the database
        List<SurveyResponse> surveyResponseList = surveyResponseRepository.findAll();
        assertThat(surveyResponseList).hasSize(databaseSizeBeforeCreate);

        // Validate the SurveyResponse in Elasticsearch
        verify(mockSurveyResponseSearchRepository, times(0)).save(surveyResponse);
    }

    @Test
    @Transactional
    public void getAllSurveyResponses() throws Exception {
        // Initialize the database
        surveyResponseRepository.saveAndFlush(surveyResponse);

        // Get all the surveyResponseList
        restSurveyResponseMockMvc.perform(get("/api/survey-responses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surveyResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].startedat").value(hasItem(DEFAULT_STARTEDAT.toString())))
            .andExpect(jsonPath("$.[*].completedat").value(hasItem(DEFAULT_COMPLETEDAT.toString())));
    }
    
    @Test
    @Transactional
    public void getSurveyResponse() throws Exception {
        // Initialize the database
        surveyResponseRepository.saveAndFlush(surveyResponse);

        // Get the surveyResponse
        restSurveyResponseMockMvc.perform(get("/api/survey-responses/{id}", surveyResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(surveyResponse.getId().intValue()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()))
            .andExpect(jsonPath("$.startedat").value(DEFAULT_STARTEDAT.toString()))
            .andExpect(jsonPath("$.completedat").value(DEFAULT_COMPLETEDAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSurveyResponse() throws Exception {
        // Get the surveyResponse
        restSurveyResponseMockMvc.perform(get("/api/survey-responses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSurveyResponse() throws Exception {
        // Initialize the database
        surveyResponseRepository.saveAndFlush(surveyResponse);

        int databaseSizeBeforeUpdate = surveyResponseRepository.findAll().size();

        // Update the surveyResponse
        SurveyResponse updatedSurveyResponse = surveyResponseRepository.findById(surveyResponse.getId()).get();
        // Disconnect from session so that the updates on updatedSurveyResponse are not directly saved in db
        em.detach(updatedSurveyResponse);
        updatedSurveyResponse
            .updated(UPDATED_UPDATED)
            .startedat(UPDATED_STARTEDAT)
            .completedat(UPDATED_COMPLETEDAT);
        SurveyResponseDTO surveyResponseDTO = surveyResponseMapper.toDto(updatedSurveyResponse);

        restSurveyResponseMockMvc.perform(put("/api/survey-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyResponseDTO)))
            .andExpect(status().isOk());

        // Validate the SurveyResponse in the database
        List<SurveyResponse> surveyResponseList = surveyResponseRepository.findAll();
        assertThat(surveyResponseList).hasSize(databaseSizeBeforeUpdate);
        SurveyResponse testSurveyResponse = surveyResponseList.get(surveyResponseList.size() - 1);
        assertThat(testSurveyResponse.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testSurveyResponse.getStartedat()).isEqualTo(UPDATED_STARTEDAT);
        assertThat(testSurveyResponse.getCompletedat()).isEqualTo(UPDATED_COMPLETEDAT);

        // Validate the SurveyResponse in Elasticsearch
        verify(mockSurveyResponseSearchRepository, times(1)).save(testSurveyResponse);
    }

    @Test
    @Transactional
    public void updateNonExistingSurveyResponse() throws Exception {
        int databaseSizeBeforeUpdate = surveyResponseRepository.findAll().size();

        // Create the SurveyResponse
        SurveyResponseDTO surveyResponseDTO = surveyResponseMapper.toDto(surveyResponse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyResponseMockMvc.perform(put("/api/survey-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyResponseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SurveyResponse in the database
        List<SurveyResponse> surveyResponseList = surveyResponseRepository.findAll();
        assertThat(surveyResponseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SurveyResponse in Elasticsearch
        verify(mockSurveyResponseSearchRepository, times(0)).save(surveyResponse);
    }

    @Test
    @Transactional
    public void deleteSurveyResponse() throws Exception {
        // Initialize the database
        surveyResponseRepository.saveAndFlush(surveyResponse);

        int databaseSizeBeforeDelete = surveyResponseRepository.findAll().size();

        // Delete the surveyResponse
        restSurveyResponseMockMvc.perform(delete("/api/survey-responses/{id}", surveyResponse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SurveyResponse> surveyResponseList = surveyResponseRepository.findAll();
        assertThat(surveyResponseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SurveyResponse in Elasticsearch
        verify(mockSurveyResponseSearchRepository, times(1)).deleteById(surveyResponse.getId());
    }

    @Test
    @Transactional
    public void searchSurveyResponse() throws Exception {
        // Initialize the database
        surveyResponseRepository.saveAndFlush(surveyResponse);
        when(mockSurveyResponseSearchRepository.search(queryStringQuery("id:" + surveyResponse.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(surveyResponse), PageRequest.of(0, 1), 1));
        // Search the surveyResponse
        restSurveyResponseMockMvc.perform(get("/api/_search/survey-responses?query=id:" + surveyResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surveyResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].startedat").value(hasItem(DEFAULT_STARTEDAT.toString())))
            .andExpect(jsonPath("$.[*].completedat").value(hasItem(DEFAULT_COMPLETEDAT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurveyResponse.class);
        SurveyResponse surveyResponse1 = new SurveyResponse();
        surveyResponse1.setId(1L);
        SurveyResponse surveyResponse2 = new SurveyResponse();
        surveyResponse2.setId(surveyResponse1.getId());
        assertThat(surveyResponse1).isEqualTo(surveyResponse2);
        surveyResponse2.setId(2L);
        assertThat(surveyResponse1).isNotEqualTo(surveyResponse2);
        surveyResponse1.setId(null);
        assertThat(surveyResponse1).isNotEqualTo(surveyResponse2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurveyResponseDTO.class);
        SurveyResponseDTO surveyResponseDTO1 = new SurveyResponseDTO();
        surveyResponseDTO1.setId(1L);
        SurveyResponseDTO surveyResponseDTO2 = new SurveyResponseDTO();
        assertThat(surveyResponseDTO1).isNotEqualTo(surveyResponseDTO2);
        surveyResponseDTO2.setId(surveyResponseDTO1.getId());
        assertThat(surveyResponseDTO1).isEqualTo(surveyResponseDTO2);
        surveyResponseDTO2.setId(2L);
        assertThat(surveyResponseDTO1).isNotEqualTo(surveyResponseDTO2);
        surveyResponseDTO1.setId(null);
        assertThat(surveyResponseDTO1).isNotEqualTo(surveyResponseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(surveyResponseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(surveyResponseMapper.fromId(null)).isNull();
    }
}
