package com.vts.vee.web.rest;

import com.vts.vee.VeeApp;

import com.vts.vee.domain.ResponseChoice;
import com.vts.vee.repository.ResponseChoiceRepository;
import com.vts.vee.repository.search.ResponseChoiceSearchRepository;
import com.vts.vee.service.ResponseChoiceService;
import com.vts.vee.service.dto.ResponseChoiceDTO;
import com.vts.vee.service.mapper.ResponseChoiceMapper;
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
 * Test class for the ResponseChoiceResource REST controller.
 *
 * @see ResponseChoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VeeApp.class)
public class ResponseChoiceResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private ResponseChoiceRepository responseChoiceRepository;

    @Autowired
    private ResponseChoiceMapper responseChoiceMapper;

    @Autowired
    private ResponseChoiceService responseChoiceService;

    /**
     * This repository is mocked in the com.vts.vee.repository.search test package.
     *
     * @see com.vts.vee.repository.search.ResponseChoiceSearchRepositoryMockConfiguration
     */
    @Autowired
    private ResponseChoiceSearchRepository mockResponseChoiceSearchRepository;

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

    private MockMvc restResponseChoiceMockMvc;

    private ResponseChoice responseChoice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResponseChoiceResource responseChoiceResource = new ResponseChoiceResource(responseChoiceService);
        this.restResponseChoiceMockMvc = MockMvcBuilders.standaloneSetup(responseChoiceResource)
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
    public static ResponseChoice createEntity(EntityManager em) {
        ResponseChoice responseChoice = new ResponseChoice()
            .text(DEFAULT_TEXT);
        return responseChoice;
    }

    @Before
    public void initTest() {
        responseChoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createResponseChoice() throws Exception {
        int databaseSizeBeforeCreate = responseChoiceRepository.findAll().size();

        // Create the ResponseChoice
        ResponseChoiceDTO responseChoiceDTO = responseChoiceMapper.toDto(responseChoice);
        restResponseChoiceMockMvc.perform(post("/api/response-choices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responseChoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the ResponseChoice in the database
        List<ResponseChoice> responseChoiceList = responseChoiceRepository.findAll();
        assertThat(responseChoiceList).hasSize(databaseSizeBeforeCreate + 1);
        ResponseChoice testResponseChoice = responseChoiceList.get(responseChoiceList.size() - 1);
        assertThat(testResponseChoice.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the ResponseChoice in Elasticsearch
        verify(mockResponseChoiceSearchRepository, times(1)).save(testResponseChoice);
    }

    @Test
    @Transactional
    public void createResponseChoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = responseChoiceRepository.findAll().size();

        // Create the ResponseChoice with an existing ID
        responseChoice.setId(1L);
        ResponseChoiceDTO responseChoiceDTO = responseChoiceMapper.toDto(responseChoice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponseChoiceMockMvc.perform(post("/api/response-choices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responseChoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResponseChoice in the database
        List<ResponseChoice> responseChoiceList = responseChoiceRepository.findAll();
        assertThat(responseChoiceList).hasSize(databaseSizeBeforeCreate);

        // Validate the ResponseChoice in Elasticsearch
        verify(mockResponseChoiceSearchRepository, times(0)).save(responseChoice);
    }

    @Test
    @Transactional
    public void getAllResponseChoices() throws Exception {
        // Initialize the database
        responseChoiceRepository.saveAndFlush(responseChoice);

        // Get all the responseChoiceList
        restResponseChoiceMockMvc.perform(get("/api/response-choices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responseChoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }
    
    @Test
    @Transactional
    public void getResponseChoice() throws Exception {
        // Initialize the database
        responseChoiceRepository.saveAndFlush(responseChoice);

        // Get the responseChoice
        restResponseChoiceMockMvc.perform(get("/api/response-choices/{id}", responseChoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(responseChoice.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResponseChoice() throws Exception {
        // Get the responseChoice
        restResponseChoiceMockMvc.perform(get("/api/response-choices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResponseChoice() throws Exception {
        // Initialize the database
        responseChoiceRepository.saveAndFlush(responseChoice);

        int databaseSizeBeforeUpdate = responseChoiceRepository.findAll().size();

        // Update the responseChoice
        ResponseChoice updatedResponseChoice = responseChoiceRepository.findById(responseChoice.getId()).get();
        // Disconnect from session so that the updates on updatedResponseChoice are not directly saved in db
        em.detach(updatedResponseChoice);
        updatedResponseChoice
            .text(UPDATED_TEXT);
        ResponseChoiceDTO responseChoiceDTO = responseChoiceMapper.toDto(updatedResponseChoice);

        restResponseChoiceMockMvc.perform(put("/api/response-choices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responseChoiceDTO)))
            .andExpect(status().isOk());

        // Validate the ResponseChoice in the database
        List<ResponseChoice> responseChoiceList = responseChoiceRepository.findAll();
        assertThat(responseChoiceList).hasSize(databaseSizeBeforeUpdate);
        ResponseChoice testResponseChoice = responseChoiceList.get(responseChoiceList.size() - 1);
        assertThat(testResponseChoice.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the ResponseChoice in Elasticsearch
        verify(mockResponseChoiceSearchRepository, times(1)).save(testResponseChoice);
    }

    @Test
    @Transactional
    public void updateNonExistingResponseChoice() throws Exception {
        int databaseSizeBeforeUpdate = responseChoiceRepository.findAll().size();

        // Create the ResponseChoice
        ResponseChoiceDTO responseChoiceDTO = responseChoiceMapper.toDto(responseChoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponseChoiceMockMvc.perform(put("/api/response-choices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responseChoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResponseChoice in the database
        List<ResponseChoice> responseChoiceList = responseChoiceRepository.findAll();
        assertThat(responseChoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResponseChoice in Elasticsearch
        verify(mockResponseChoiceSearchRepository, times(0)).save(responseChoice);
    }

    @Test
    @Transactional
    public void deleteResponseChoice() throws Exception {
        // Initialize the database
        responseChoiceRepository.saveAndFlush(responseChoice);

        int databaseSizeBeforeDelete = responseChoiceRepository.findAll().size();

        // Delete the responseChoice
        restResponseChoiceMockMvc.perform(delete("/api/response-choices/{id}", responseChoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ResponseChoice> responseChoiceList = responseChoiceRepository.findAll();
        assertThat(responseChoiceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ResponseChoice in Elasticsearch
        verify(mockResponseChoiceSearchRepository, times(1)).deleteById(responseChoice.getId());
    }

    @Test
    @Transactional
    public void searchResponseChoice() throws Exception {
        // Initialize the database
        responseChoiceRepository.saveAndFlush(responseChoice);
        when(mockResponseChoiceSearchRepository.search(queryStringQuery("id:" + responseChoice.getId())))
            .thenReturn(Collections.singletonList(responseChoice));
        // Search the responseChoice
        restResponseChoiceMockMvc.perform(get("/api/_search/response-choices?query=id:" + responseChoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responseChoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponseChoice.class);
        ResponseChoice responseChoice1 = new ResponseChoice();
        responseChoice1.setId(1L);
        ResponseChoice responseChoice2 = new ResponseChoice();
        responseChoice2.setId(responseChoice1.getId());
        assertThat(responseChoice1).isEqualTo(responseChoice2);
        responseChoice2.setId(2L);
        assertThat(responseChoice1).isNotEqualTo(responseChoice2);
        responseChoice1.setId(null);
        assertThat(responseChoice1).isNotEqualTo(responseChoice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponseChoiceDTO.class);
        ResponseChoiceDTO responseChoiceDTO1 = new ResponseChoiceDTO();
        responseChoiceDTO1.setId(1L);
        ResponseChoiceDTO responseChoiceDTO2 = new ResponseChoiceDTO();
        assertThat(responseChoiceDTO1).isNotEqualTo(responseChoiceDTO2);
        responseChoiceDTO2.setId(responseChoiceDTO1.getId());
        assertThat(responseChoiceDTO1).isEqualTo(responseChoiceDTO2);
        responseChoiceDTO2.setId(2L);
        assertThat(responseChoiceDTO1).isNotEqualTo(responseChoiceDTO2);
        responseChoiceDTO1.setId(null);
        assertThat(responseChoiceDTO1).isNotEqualTo(responseChoiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(responseChoiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(responseChoiceMapper.fromId(null)).isNull();
    }
}
