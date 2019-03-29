package com.vts.vee.web.rest;

import com.vts.vee.VeeApp;

import com.vts.vee.domain.Respondent;
import com.vts.vee.repository.RespondentRepository;
import com.vts.vee.repository.search.RespondentSearchRepository;
import com.vts.vee.service.RespondentService;
import com.vts.vee.service.dto.RespondentDTO;
import com.vts.vee.service.mapper.RespondentMapper;
import com.vts.vee.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import com.vts.vee.domain.enumeration.Gender;
/**
 * Test class for the RespondentResource REST controller.
 *
 * @see RespondentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VeeApp.class)
public class RespondentResourceIntTest {

    private static final String DEFAULT_AVATAR_URL = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_URL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Gender DEFAULT_GENDER = Gender.Male;
    private static final Gender UPDATED_GENDER = Gender.Female;

    @Autowired
    private RespondentRepository respondentRepository;

    @Autowired
    private RespondentMapper respondentMapper;

    @Autowired
    private RespondentService respondentService;

    /**
     * This repository is mocked in the com.vts.vee.repository.search test package.
     *
     * @see com.vts.vee.repository.search.RespondentSearchRepositoryMockConfiguration
     */
    @Autowired
    private RespondentSearchRepository mockRespondentSearchRepository;

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

    private MockMvc restRespondentMockMvc;

    private Respondent respondent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RespondentResource respondentResource = new RespondentResource(respondentService);
        this.restRespondentMockMvc = MockMvcBuilders.standaloneSetup(respondentResource)
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
    public static Respondent createEntity(EntityManager em) {
        Respondent respondent = new Respondent()
            .avatarUrl(DEFAULT_AVATAR_URL)
            .email(DEFAULT_EMAIL)
            .birthDay(DEFAULT_BIRTH_DAY)
            .gender(DEFAULT_GENDER);
        return respondent;
    }

    @Before
    public void initTest() {
        respondent = createEntity(em);
    }

    @Test
    @Transactional
    public void createRespondent() throws Exception {
        int databaseSizeBeforeCreate = respondentRepository.findAll().size();

        // Create the Respondent
        RespondentDTO respondentDTO = respondentMapper.toDto(respondent);
        restRespondentMockMvc.perform(post("/api/respondents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respondentDTO)))
            .andExpect(status().isCreated());

        // Validate the Respondent in the database
        List<Respondent> respondentList = respondentRepository.findAll();
        assertThat(respondentList).hasSize(databaseSizeBeforeCreate + 1);
        Respondent testRespondent = respondentList.get(respondentList.size() - 1);
        assertThat(testRespondent.getAvatarUrl()).isEqualTo(DEFAULT_AVATAR_URL);
        assertThat(testRespondent.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRespondent.getBirthDay()).isEqualTo(DEFAULT_BIRTH_DAY);
        assertThat(testRespondent.getGender()).isEqualTo(DEFAULT_GENDER);

        // Validate the Respondent in Elasticsearch
        verify(mockRespondentSearchRepository, times(1)).save(testRespondent);
    }

    @Test
    @Transactional
    public void createRespondentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = respondentRepository.findAll().size();

        // Create the Respondent with an existing ID
        respondent.setId(1L);
        RespondentDTO respondentDTO = respondentMapper.toDto(respondent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRespondentMockMvc.perform(post("/api/respondents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respondentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Respondent in the database
        List<Respondent> respondentList = respondentRepository.findAll();
        assertThat(respondentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Respondent in Elasticsearch
        verify(mockRespondentSearchRepository, times(0)).save(respondent);
    }

    @Test
    @Transactional
    public void getAllRespondents() throws Exception {
        // Initialize the database
        respondentRepository.saveAndFlush(respondent);

        // Get all the respondentList
        restRespondentMockMvc.perform(get("/api/respondents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(respondent.getId().intValue())))
            .andExpect(jsonPath("$.[*].avatarUrl").value(hasItem(DEFAULT_AVATAR_URL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }
    
    @Test
    @Transactional
    public void getRespondent() throws Exception {
        // Initialize the database
        respondentRepository.saveAndFlush(respondent);

        // Get the respondent
        restRespondentMockMvc.perform(get("/api/respondents/{id}", respondent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(respondent.getId().intValue()))
            .andExpect(jsonPath("$.avatarUrl").value(DEFAULT_AVATAR_URL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.birthDay").value(DEFAULT_BIRTH_DAY.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRespondent() throws Exception {
        // Get the respondent
        restRespondentMockMvc.perform(get("/api/respondents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRespondent() throws Exception {
        // Initialize the database
        respondentRepository.saveAndFlush(respondent);

        int databaseSizeBeforeUpdate = respondentRepository.findAll().size();

        // Update the respondent
        Respondent updatedRespondent = respondentRepository.findById(respondent.getId()).get();
        // Disconnect from session so that the updates on updatedRespondent are not directly saved in db
        em.detach(updatedRespondent);
        updatedRespondent
            .avatarUrl(UPDATED_AVATAR_URL)
            .email(UPDATED_EMAIL)
            .birthDay(UPDATED_BIRTH_DAY)
            .gender(UPDATED_GENDER);
        RespondentDTO respondentDTO = respondentMapper.toDto(updatedRespondent);

        restRespondentMockMvc.perform(put("/api/respondents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respondentDTO)))
            .andExpect(status().isOk());

        // Validate the Respondent in the database
        List<Respondent> respondentList = respondentRepository.findAll();
        assertThat(respondentList).hasSize(databaseSizeBeforeUpdate);
        Respondent testRespondent = respondentList.get(respondentList.size() - 1);
        assertThat(testRespondent.getAvatarUrl()).isEqualTo(UPDATED_AVATAR_URL);
        assertThat(testRespondent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRespondent.getBirthDay()).isEqualTo(UPDATED_BIRTH_DAY);
        assertThat(testRespondent.getGender()).isEqualTo(UPDATED_GENDER);

        // Validate the Respondent in Elasticsearch
        verify(mockRespondentSearchRepository, times(1)).save(testRespondent);
    }

    @Test
    @Transactional
    public void updateNonExistingRespondent() throws Exception {
        int databaseSizeBeforeUpdate = respondentRepository.findAll().size();

        // Create the Respondent
        RespondentDTO respondentDTO = respondentMapper.toDto(respondent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRespondentMockMvc.perform(put("/api/respondents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respondentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Respondent in the database
        List<Respondent> respondentList = respondentRepository.findAll();
        assertThat(respondentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Respondent in Elasticsearch
        verify(mockRespondentSearchRepository, times(0)).save(respondent);
    }

    @Test
    @Transactional
    public void deleteRespondent() throws Exception {
        // Initialize the database
        respondentRepository.saveAndFlush(respondent);

        int databaseSizeBeforeDelete = respondentRepository.findAll().size();

        // Delete the respondent
        restRespondentMockMvc.perform(delete("/api/respondents/{id}", respondent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Respondent> respondentList = respondentRepository.findAll();
        assertThat(respondentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Respondent in Elasticsearch
        verify(mockRespondentSearchRepository, times(1)).deleteById(respondent.getId());
    }

    @Test
    @Transactional
    public void searchRespondent() throws Exception {
        // Initialize the database
        respondentRepository.saveAndFlush(respondent);
        when(mockRespondentSearchRepository.search(queryStringQuery("id:" + respondent.getId())))
            .thenReturn(Collections.singletonList(respondent));
        // Search the respondent
        restRespondentMockMvc.perform(get("/api/_search/respondents?query=id:" + respondent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(respondent.getId().intValue())))
            .andExpect(jsonPath("$.[*].avatarUrl").value(hasItem(DEFAULT_AVATAR_URL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Respondent.class);
        Respondent respondent1 = new Respondent();
        respondent1.setId(1L);
        Respondent respondent2 = new Respondent();
        respondent2.setId(respondent1.getId());
        assertThat(respondent1).isEqualTo(respondent2);
        respondent2.setId(2L);
        assertThat(respondent1).isNotEqualTo(respondent2);
        respondent1.setId(null);
        assertThat(respondent1).isNotEqualTo(respondent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RespondentDTO.class);
        RespondentDTO respondentDTO1 = new RespondentDTO();
        respondentDTO1.setId(1L);
        RespondentDTO respondentDTO2 = new RespondentDTO();
        assertThat(respondentDTO1).isNotEqualTo(respondentDTO2);
        respondentDTO2.setId(respondentDTO1.getId());
        assertThat(respondentDTO1).isEqualTo(respondentDTO2);
        respondentDTO2.setId(2L);
        assertThat(respondentDTO1).isNotEqualTo(respondentDTO2);
        respondentDTO1.setId(null);
        assertThat(respondentDTO1).isNotEqualTo(respondentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(respondentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(respondentMapper.fromId(null)).isNull();
    }
}
