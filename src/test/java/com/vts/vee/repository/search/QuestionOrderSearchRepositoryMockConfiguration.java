package com.vts.vee.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of QuestionOrderSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class QuestionOrderSearchRepositoryMockConfiguration {

    @MockBean
    private QuestionOrderSearchRepository mockQuestionOrderSearchRepository;

}
