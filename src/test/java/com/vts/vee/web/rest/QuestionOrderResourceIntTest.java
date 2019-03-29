package com.vts.vee.web.rest;

import com.vts.vee.VeeApp;

import com.vts.vee.domain.QuestionOrder;
import com.vts.vee.repository.QuestionOrderRepository;
import com.vts.vee.repository.search.QuestionOrderSearchRepository;
import com.vts.vee.service.QuestionOrderService;
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
 * Test class for the QuestionOrderResource REST controller.
 *
 * @see QuestionOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VeeApp.class)
public class QuestionOrderResourceIntTest {

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    @Autowired
    private QuestionOrderRepository questionOrderRepository;

    @Autowired
    private QuestionOrderService questionOrderService;

    /**
     * This repository is mocked in the com.vts.vee.repository.search test package.
     *
     * @see com.vts.vee.repository.search.QuestionOrderSearchRepositoryMockConfiguration
     */
    @Autowired
    private QuestionOrderSearchRepository mockQuestionOrderSearchRepository;

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

    private MockMvc restQuestionOrderMockMvc;

    private QuestionOrder questionOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionOrderResource questionOrderResource = new QuestionOrderResource(questionOrderService);
        this.restQuestionOrderMockMvc = MockMvcBuilders.standaloneSetup(questionOrderResource)
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
    public static QuestionOrder createEntity(EntityManager em) {
        QuestionOrder questionOrder = new QuestionOrder()
            .order(DEFAULT_ORDER);
        return questionOrder;
    }

    @Before
    public void initTest() {
        questionOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestionOrder() throws Exception {
        int databaseSizeBeforeCreate = questionOrderRepository.findAll().size();

        // Create the QuestionOrder
        restQuestionOrderMockMvc.perform(post("/api/question-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionOrder)))
            .andExpect(status().isCreated());

        // Validate the QuestionOrder in the database
        List<QuestionOrder> questionOrderList = questionOrderRepository.findAll();
        assertThat(questionOrderList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionOrder testQuestionOrder = questionOrderList.get(questionOrderList.size() - 1);
        assertThat(testQuestionOrder.getOrder()).isEqualTo(DEFAULT_ORDER);

        // Validate the QuestionOrder in Elasticsearch
        verify(mockQuestionOrderSearchRepository, times(1)).save(testQuestionOrder);
    }

    @Test
    @Transactional
    public void createQuestionOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionOrderRepository.findAll().size();

        // Create the QuestionOrder with an existing ID
        questionOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionOrderMockMvc.perform(post("/api/question-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionOrder)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionOrder in the database
        List<QuestionOrder> questionOrderList = questionOrderRepository.findAll();
        assertThat(questionOrderList).hasSize(databaseSizeBeforeCreate);

        // Validate the QuestionOrder in Elasticsearch
        verify(mockQuestionOrderSearchRepository, times(0)).save(questionOrder);
    }

    @Test
    @Transactional
    public void getAllQuestionOrders() throws Exception {
        // Initialize the database
        questionOrderRepository.saveAndFlush(questionOrder);

        // Get all the questionOrderList
        restQuestionOrderMockMvc.perform(get("/api/question-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }
    
    @Test
    @Transactional
    public void getQuestionOrder() throws Exception {
        // Initialize the database
        questionOrderRepository.saveAndFlush(questionOrder);

        // Get the questionOrder
        restQuestionOrderMockMvc.perform(get("/api/question-orders/{id}", questionOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(questionOrder.getId().intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionOrder() throws Exception {
        // Get the questionOrder
        restQuestionOrderMockMvc.perform(get("/api/question-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionOrder() throws Exception {
        // Initialize the database
        questionOrderService.save(questionOrder);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockQuestionOrderSearchRepository);

        int databaseSizeBeforeUpdate = questionOrderRepository.findAll().size();

        // Update the questionOrder
        QuestionOrder updatedQuestionOrder = questionOrderRepository.findById(questionOrder.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionOrder are not directly saved in db
        em.detach(updatedQuestionOrder);
        updatedQuestionOrder
            .order(UPDATED_ORDER);

        restQuestionOrderMockMvc.perform(put("/api/question-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestionOrder)))
            .andExpect(status().isOk());

        // Validate the QuestionOrder in the database
        List<QuestionOrder> questionOrderList = questionOrderRepository.findAll();
        assertThat(questionOrderList).hasSize(databaseSizeBeforeUpdate);
        QuestionOrder testQuestionOrder = questionOrderList.get(questionOrderList.size() - 1);
        assertThat(testQuestionOrder.getOrder()).isEqualTo(UPDATED_ORDER);

        // Validate the QuestionOrder in Elasticsearch
        verify(mockQuestionOrderSearchRepository, times(1)).save(testQuestionOrder);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestionOrder() throws Exception {
        int databaseSizeBeforeUpdate = questionOrderRepository.findAll().size();

        // Create the QuestionOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionOrderMockMvc.perform(put("/api/question-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionOrder)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionOrder in the database
        List<QuestionOrder> questionOrderList = questionOrderRepository.findAll();
        assertThat(questionOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuestionOrder in Elasticsearch
        verify(mockQuestionOrderSearchRepository, times(0)).save(questionOrder);
    }

    @Test
    @Transactional
    public void deleteQuestionOrder() throws Exception {
        // Initialize the database
        questionOrderService.save(questionOrder);

        int databaseSizeBeforeDelete = questionOrderRepository.findAll().size();

        // Delete the questionOrder
        restQuestionOrderMockMvc.perform(delete("/api/question-orders/{id}", questionOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuestionOrder> questionOrderList = questionOrderRepository.findAll();
        assertThat(questionOrderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the QuestionOrder in Elasticsearch
        verify(mockQuestionOrderSearchRepository, times(1)).deleteById(questionOrder.getId());
    }

    @Test
    @Transactional
    public void searchQuestionOrder() throws Exception {
        // Initialize the database
        questionOrderService.save(questionOrder);
        when(mockQuestionOrderSearchRepository.search(queryStringQuery("id:" + questionOrder.getId())))
            .thenReturn(Collections.singletonList(questionOrder));
        // Search the questionOrder
        restQuestionOrderMockMvc.perform(get("/api/_search/question-orders?query=id:" + questionOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionOrder.class);
        QuestionOrder questionOrder1 = new QuestionOrder();
        questionOrder1.setId(1L);
        QuestionOrder questionOrder2 = new QuestionOrder();
        questionOrder2.setId(questionOrder1.getId());
        assertThat(questionOrder1).isEqualTo(questionOrder2);
        questionOrder2.setId(2L);
        assertThat(questionOrder1).isNotEqualTo(questionOrder2);
        questionOrder1.setId(null);
        assertThat(questionOrder1).isNotEqualTo(questionOrder2);
    }
}
