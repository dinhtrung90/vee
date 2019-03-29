package com.vts.vee.web.rest;

import com.vts.vee.VeeApp;

import com.vts.vee.domain.Survey;
import com.vts.vee.repository.SurveyRepository;
import com.vts.vee.repository.search.SurveySearchRepository;
import com.vts.vee.service.SurveyService;
import com.vts.vee.service.dto.SurveyDTO;
import com.vts.vee.service.mapper.SurveyMapper;
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
 * Test class for the SurveyResource REST controller.
 *
 * @see SurveyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VeeApp.class)
public class SurveyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OPENINGTIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OPENINGTIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSINGTIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSINGTIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private SurveyService surveyService;

    /**
     * This repository is mocked in the com.vts.vee.repository.search test package.
     *
     * @see com.vts.vee.repository.search.SurveySearchRepositoryMockConfiguration
     */
    @Autowired
    private SurveySearchRepository mockSurveySearchRepository;

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

    private MockMvc restSurveyMockMvc;

    private Survey survey;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SurveyResource surveyResource = new SurveyResource(surveyService);
        this.restSurveyMockMvc = MockMvcBuilders.standaloneSetup(surveyResource)
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
    public static Survey createEntity(EntityManager em) {
        Survey survey = new Survey()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .updated(DEFAULT_UPDATED)
            .openingtime(DEFAULT_OPENINGTIME)
            .closingtime(DEFAULT_CLOSINGTIME);
        return survey;
    }

    @Before
    public void initTest() {
        survey = createEntity(em);
    }

    @Test
    @Transactional
    public void createSurvey() throws Exception {
        int databaseSizeBeforeCreate = surveyRepository.findAll().size();

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);
        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isCreated());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeCreate + 1);
        Survey testSurvey = surveyList.get(surveyList.size() - 1);
        assertThat(testSurvey.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSurvey.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSurvey.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testSurvey.getOpeningtime()).isEqualTo(DEFAULT_OPENINGTIME);
        assertThat(testSurvey.getClosingtime()).isEqualTo(DEFAULT_CLOSINGTIME);

        // Validate the Survey in Elasticsearch
        verify(mockSurveySearchRepository, times(1)).save(testSurvey);
    }

    @Test
    @Transactional
    public void createSurveyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = surveyRepository.findAll().size();

        // Create the Survey with an existing ID
        survey.setId(1L);
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeCreate);

        // Validate the Survey in Elasticsearch
        verify(mockSurveySearchRepository, times(0)).save(survey);
    }

    @Test
    @Transactional
    public void getAllSurveys() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        // Get all the surveyList
        restSurveyMockMvc.perform(get("/api/surveys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].openingtime").value(hasItem(DEFAULT_OPENINGTIME.toString())))
            .andExpect(jsonPath("$.[*].closingtime").value(hasItem(DEFAULT_CLOSINGTIME.toString())));
    }
    
    @Test
    @Transactional
    public void getSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        // Get the survey
        restSurveyMockMvc.perform(get("/api/surveys/{id}", survey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(survey.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()))
            .andExpect(jsonPath("$.openingtime").value(DEFAULT_OPENINGTIME.toString()))
            .andExpect(jsonPath("$.closingtime").value(DEFAULT_CLOSINGTIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSurvey() throws Exception {
        // Get the survey
        restSurveyMockMvc.perform(get("/api/surveys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

        // Update the survey
        Survey updatedSurvey = surveyRepository.findById(survey.getId()).get();
        // Disconnect from session so that the updates on updatedSurvey are not directly saved in db
        em.detach(updatedSurvey);
        updatedSurvey
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .updated(UPDATED_UPDATED)
            .openingtime(UPDATED_OPENINGTIME)
            .closingtime(UPDATED_CLOSINGTIME);
        SurveyDTO surveyDTO = surveyMapper.toDto(updatedSurvey);

        restSurveyMockMvc.perform(put("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isOk());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
        Survey testSurvey = surveyList.get(surveyList.size() - 1);
        assertThat(testSurvey.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSurvey.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSurvey.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testSurvey.getOpeningtime()).isEqualTo(UPDATED_OPENINGTIME);
        assertThat(testSurvey.getClosingtime()).isEqualTo(UPDATED_CLOSINGTIME);

        // Validate the Survey in Elasticsearch
        verify(mockSurveySearchRepository, times(1)).save(testSurvey);
    }

    @Test
    @Transactional
    public void updateNonExistingSurvey() throws Exception {
        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyMockMvc.perform(put("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Survey in Elasticsearch
        verify(mockSurveySearchRepository, times(0)).save(survey);
    }

    @Test
    @Transactional
    public void deleteSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        int databaseSizeBeforeDelete = surveyRepository.findAll().size();

        // Delete the survey
        restSurveyMockMvc.perform(delete("/api/surveys/{id}", survey.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Survey in Elasticsearch
        verify(mockSurveySearchRepository, times(1)).deleteById(survey.getId());
    }

    @Test
    @Transactional
    public void searchSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);
        when(mockSurveySearchRepository.search(queryStringQuery("id:" + survey.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(survey), PageRequest.of(0, 1), 1));
        // Search the survey
        restSurveyMockMvc.perform(get("/api/_search/surveys?query=id:" + survey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].openingtime").value(hasItem(DEFAULT_OPENINGTIME.toString())))
            .andExpect(jsonPath("$.[*].closingtime").value(hasItem(DEFAULT_CLOSINGTIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Survey.class);
        Survey survey1 = new Survey();
        survey1.setId(1L);
        Survey survey2 = new Survey();
        survey2.setId(survey1.getId());
        assertThat(survey1).isEqualTo(survey2);
        survey2.setId(2L);
        assertThat(survey1).isNotEqualTo(survey2);
        survey1.setId(null);
        assertThat(survey1).isNotEqualTo(survey2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurveyDTO.class);
        SurveyDTO surveyDTO1 = new SurveyDTO();
        surveyDTO1.setId(1L);
        SurveyDTO surveyDTO2 = new SurveyDTO();
        assertThat(surveyDTO1).isNotEqualTo(surveyDTO2);
        surveyDTO2.setId(surveyDTO1.getId());
        assertThat(surveyDTO1).isEqualTo(surveyDTO2);
        surveyDTO2.setId(2L);
        assertThat(surveyDTO1).isNotEqualTo(surveyDTO2);
        surveyDTO1.setId(null);
        assertThat(surveyDTO1).isNotEqualTo(surveyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(surveyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(surveyMapper.fromId(null)).isNull();
    }
}
