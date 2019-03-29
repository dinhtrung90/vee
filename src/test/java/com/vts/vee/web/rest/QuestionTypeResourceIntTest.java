package com.vts.vee.web.rest;

import com.vts.vee.VeeApp;

import com.vts.vee.domain.QuestionType;
import com.vts.vee.repository.QuestionTypeRepository;
import com.vts.vee.repository.search.QuestionTypeSearchRepository;
import com.vts.vee.service.QuestionTypeService;
import com.vts.vee.service.dto.QuestionTypeDTO;
import com.vts.vee.service.mapper.QuestionTypeMapper;
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
 * Test class for the QuestionTypeResource REST controller.
 *
 * @see QuestionTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VeeApp.class)
public class QuestionTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private QuestionTypeRepository questionTypeRepository;

    @Autowired
    private QuestionTypeMapper questionTypeMapper;

    @Autowired
    private QuestionTypeService questionTypeService;

    /**
     * This repository is mocked in the com.vts.vee.repository.search test package.
     *
     * @see com.vts.vee.repository.search.QuestionTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private QuestionTypeSearchRepository mockQuestionTypeSearchRepository;

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

    private MockMvc restQuestionTypeMockMvc;

    private QuestionType questionType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionTypeResource questionTypeResource = new QuestionTypeResource(questionTypeService);
        this.restQuestionTypeMockMvc = MockMvcBuilders.standaloneSetup(questionTypeResource)
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
    public static QuestionType createEntity(EntityManager em) {
        QuestionType questionType = new QuestionType()
            .name(DEFAULT_NAME);
        return questionType;
    }

    @Before
    public void initTest() {
        questionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestionType() throws Exception {
        int databaseSizeBeforeCreate = questionTypeRepository.findAll().size();

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);
        restQuestionTypeMockMvc.perform(post("/api/question-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionType testQuestionType = questionTypeList.get(questionTypeList.size() - 1);
        assertThat(testQuestionType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the QuestionType in Elasticsearch
        verify(mockQuestionTypeSearchRepository, times(1)).save(testQuestionType);
    }

    @Test
    @Transactional
    public void createQuestionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionTypeRepository.findAll().size();

        // Create the QuestionType with an existing ID
        questionType.setId(1L);
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionTypeMockMvc.perform(post("/api/question-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the QuestionType in Elasticsearch
        verify(mockQuestionTypeSearchRepository, times(0)).save(questionType);
    }

    @Test
    @Transactional
    public void getAllQuestionTypes() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList
        restQuestionTypeMockMvc.perform(get("/api/question-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getQuestionType() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get the questionType
        restQuestionTypeMockMvc.perform(get("/api/question-types/{id}", questionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(questionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionType() throws Exception {
        // Get the questionType
        restQuestionTypeMockMvc.perform(get("/api/question-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionType() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();

        // Update the questionType
        QuestionType updatedQuestionType = questionTypeRepository.findById(questionType.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionType are not directly saved in db
        em.detach(updatedQuestionType);
        updatedQuestionType
            .name(UPDATED_NAME);
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(updatedQuestionType);

        restQuestionTypeMockMvc.perform(put("/api/question-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionTypeDTO)))
            .andExpect(status().isOk());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
        QuestionType testQuestionType = questionTypeList.get(questionTypeList.size() - 1);
        assertThat(testQuestionType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the QuestionType in Elasticsearch
        verify(mockQuestionTypeSearchRepository, times(1)).save(testQuestionType);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestionType() throws Exception {
        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc.perform(put("/api/question-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuestionType in Elasticsearch
        verify(mockQuestionTypeSearchRepository, times(0)).save(questionType);
    }

    @Test
    @Transactional
    public void deleteQuestionType() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        int databaseSizeBeforeDelete = questionTypeRepository.findAll().size();

        // Delete the questionType
        restQuestionTypeMockMvc.perform(delete("/api/question-types/{id}", questionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the QuestionType in Elasticsearch
        verify(mockQuestionTypeSearchRepository, times(1)).deleteById(questionType.getId());
    }

    @Test
    @Transactional
    public void searchQuestionType() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);
        when(mockQuestionTypeSearchRepository.search(queryStringQuery("id:" + questionType.getId())))
            .thenReturn(Collections.singletonList(questionType));
        // Search the questionType
        restQuestionTypeMockMvc.perform(get("/api/_search/question-types?query=id:" + questionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionType.class);
        QuestionType questionType1 = new QuestionType();
        questionType1.setId(1L);
        QuestionType questionType2 = new QuestionType();
        questionType2.setId(questionType1.getId());
        assertThat(questionType1).isEqualTo(questionType2);
        questionType2.setId(2L);
        assertThat(questionType1).isNotEqualTo(questionType2);
        questionType1.setId(null);
        assertThat(questionType1).isNotEqualTo(questionType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionTypeDTO.class);
        QuestionTypeDTO questionTypeDTO1 = new QuestionTypeDTO();
        questionTypeDTO1.setId(1L);
        QuestionTypeDTO questionTypeDTO2 = new QuestionTypeDTO();
        assertThat(questionTypeDTO1).isNotEqualTo(questionTypeDTO2);
        questionTypeDTO2.setId(questionTypeDTO1.getId());
        assertThat(questionTypeDTO1).isEqualTo(questionTypeDTO2);
        questionTypeDTO2.setId(2L);
        assertThat(questionTypeDTO1).isNotEqualTo(questionTypeDTO2);
        questionTypeDTO1.setId(null);
        assertThat(questionTypeDTO1).isNotEqualTo(questionTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(questionTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(questionTypeMapper.fromId(null)).isNull();
    }
}
