package com.vts.vee.web.rest;

import com.vts.vee.VeeApp;

import com.vts.vee.domain.VeeResponse;
import com.vts.vee.repository.VeeResponseRepository;
import com.vts.vee.repository.search.VeeResponseSearchRepository;
import com.vts.vee.service.VeeResponseService;
import com.vts.vee.service.dto.VeeResponseDTO;
import com.vts.vee.service.mapper.VeeResponseMapper;
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
 * Test class for the VeeResponseResource REST controller.
 *
 * @see VeeResponseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VeeApp.class)
public class VeeResponseResourceIntTest {

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    @Autowired
    private VeeResponseRepository veeResponseRepository;

    @Autowired
    private VeeResponseMapper veeResponseMapper;

    @Autowired
    private VeeResponseService veeResponseService;

    /**
     * This repository is mocked in the com.vts.vee.repository.search test package.
     *
     * @see com.vts.vee.repository.search.VeeResponseSearchRepositoryMockConfiguration
     */
    @Autowired
    private VeeResponseSearchRepository mockVeeResponseSearchRepository;

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

    private MockMvc restVeeResponseMockMvc;

    private VeeResponse veeResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VeeResponseResource veeResponseResource = new VeeResponseResource(veeResponseService);
        this.restVeeResponseMockMvc = MockMvcBuilders.standaloneSetup(veeResponseResource)
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
    public static VeeResponse createEntity(EntityManager em) {
        VeeResponse veeResponse = new VeeResponse()
            .answer(DEFAULT_ANSWER);
        return veeResponse;
    }

    @Before
    public void initTest() {
        veeResponse = createEntity(em);
    }

    @Test
    @Transactional
    public void createVeeResponse() throws Exception {
        int databaseSizeBeforeCreate = veeResponseRepository.findAll().size();

        // Create the VeeResponse
        VeeResponseDTO veeResponseDTO = veeResponseMapper.toDto(veeResponse);
        restVeeResponseMockMvc.perform(post("/api/vee-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veeResponseDTO)))
            .andExpect(status().isCreated());

        // Validate the VeeResponse in the database
        List<VeeResponse> veeResponseList = veeResponseRepository.findAll();
        assertThat(veeResponseList).hasSize(databaseSizeBeforeCreate + 1);
        VeeResponse testVeeResponse = veeResponseList.get(veeResponseList.size() - 1);
        assertThat(testVeeResponse.getAnswer()).isEqualTo(DEFAULT_ANSWER);

        // Validate the VeeResponse in Elasticsearch
        verify(mockVeeResponseSearchRepository, times(1)).save(testVeeResponse);
    }

    @Test
    @Transactional
    public void createVeeResponseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = veeResponseRepository.findAll().size();

        // Create the VeeResponse with an existing ID
        veeResponse.setId(1L);
        VeeResponseDTO veeResponseDTO = veeResponseMapper.toDto(veeResponse);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVeeResponseMockMvc.perform(post("/api/vee-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veeResponseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VeeResponse in the database
        List<VeeResponse> veeResponseList = veeResponseRepository.findAll();
        assertThat(veeResponseList).hasSize(databaseSizeBeforeCreate);

        // Validate the VeeResponse in Elasticsearch
        verify(mockVeeResponseSearchRepository, times(0)).save(veeResponse);
    }

    @Test
    @Transactional
    public void getAllVeeResponses() throws Exception {
        // Initialize the database
        veeResponseRepository.saveAndFlush(veeResponse);

        // Get all the veeResponseList
        restVeeResponseMockMvc.perform(get("/api/vee-responses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veeResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())));
    }
    
    @Test
    @Transactional
    public void getVeeResponse() throws Exception {
        // Initialize the database
        veeResponseRepository.saveAndFlush(veeResponse);

        // Get the veeResponse
        restVeeResponseMockMvc.perform(get("/api/vee-responses/{id}", veeResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(veeResponse.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVeeResponse() throws Exception {
        // Get the veeResponse
        restVeeResponseMockMvc.perform(get("/api/vee-responses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVeeResponse() throws Exception {
        // Initialize the database
        veeResponseRepository.saveAndFlush(veeResponse);

        int databaseSizeBeforeUpdate = veeResponseRepository.findAll().size();

        // Update the veeResponse
        VeeResponse updatedVeeResponse = veeResponseRepository.findById(veeResponse.getId()).get();
        // Disconnect from session so that the updates on updatedVeeResponse are not directly saved in db
        em.detach(updatedVeeResponse);
        updatedVeeResponse
            .answer(UPDATED_ANSWER);
        VeeResponseDTO veeResponseDTO = veeResponseMapper.toDto(updatedVeeResponse);

        restVeeResponseMockMvc.perform(put("/api/vee-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veeResponseDTO)))
            .andExpect(status().isOk());

        // Validate the VeeResponse in the database
        List<VeeResponse> veeResponseList = veeResponseRepository.findAll();
        assertThat(veeResponseList).hasSize(databaseSizeBeforeUpdate);
        VeeResponse testVeeResponse = veeResponseList.get(veeResponseList.size() - 1);
        assertThat(testVeeResponse.getAnswer()).isEqualTo(UPDATED_ANSWER);

        // Validate the VeeResponse in Elasticsearch
        verify(mockVeeResponseSearchRepository, times(1)).save(testVeeResponse);
    }

    @Test
    @Transactional
    public void updateNonExistingVeeResponse() throws Exception {
        int databaseSizeBeforeUpdate = veeResponseRepository.findAll().size();

        // Create the VeeResponse
        VeeResponseDTO veeResponseDTO = veeResponseMapper.toDto(veeResponse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeeResponseMockMvc.perform(put("/api/vee-responses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veeResponseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VeeResponse in the database
        List<VeeResponse> veeResponseList = veeResponseRepository.findAll();
        assertThat(veeResponseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the VeeResponse in Elasticsearch
        verify(mockVeeResponseSearchRepository, times(0)).save(veeResponse);
    }

    @Test
    @Transactional
    public void deleteVeeResponse() throws Exception {
        // Initialize the database
        veeResponseRepository.saveAndFlush(veeResponse);

        int databaseSizeBeforeDelete = veeResponseRepository.findAll().size();

        // Delete the veeResponse
        restVeeResponseMockMvc.perform(delete("/api/vee-responses/{id}", veeResponse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VeeResponse> veeResponseList = veeResponseRepository.findAll();
        assertThat(veeResponseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the VeeResponse in Elasticsearch
        verify(mockVeeResponseSearchRepository, times(1)).deleteById(veeResponse.getId());
    }

    @Test
    @Transactional
    public void searchVeeResponse() throws Exception {
        // Initialize the database
        veeResponseRepository.saveAndFlush(veeResponse);
        when(mockVeeResponseSearchRepository.search(queryStringQuery("id:" + veeResponse.getId())))
            .thenReturn(Collections.singletonList(veeResponse));
        // Search the veeResponse
        restVeeResponseMockMvc.perform(get("/api/_search/vee-responses?query=id:" + veeResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veeResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VeeResponse.class);
        VeeResponse veeResponse1 = new VeeResponse();
        veeResponse1.setId(1L);
        VeeResponse veeResponse2 = new VeeResponse();
        veeResponse2.setId(veeResponse1.getId());
        assertThat(veeResponse1).isEqualTo(veeResponse2);
        veeResponse2.setId(2L);
        assertThat(veeResponse1).isNotEqualTo(veeResponse2);
        veeResponse1.setId(null);
        assertThat(veeResponse1).isNotEqualTo(veeResponse2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VeeResponseDTO.class);
        VeeResponseDTO veeResponseDTO1 = new VeeResponseDTO();
        veeResponseDTO1.setId(1L);
        VeeResponseDTO veeResponseDTO2 = new VeeResponseDTO();
        assertThat(veeResponseDTO1).isNotEqualTo(veeResponseDTO2);
        veeResponseDTO2.setId(veeResponseDTO1.getId());
        assertThat(veeResponseDTO1).isEqualTo(veeResponseDTO2);
        veeResponseDTO2.setId(2L);
        assertThat(veeResponseDTO1).isNotEqualTo(veeResponseDTO2);
        veeResponseDTO1.setId(null);
        assertThat(veeResponseDTO1).isNotEqualTo(veeResponseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(veeResponseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(veeResponseMapper.fromId(null)).isNull();
    }
}
