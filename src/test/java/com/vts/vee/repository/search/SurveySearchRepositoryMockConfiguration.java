package com.vts.vee.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SurveySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SurveySearchRepositoryMockConfiguration {

    @MockBean
    private SurveySearchRepository mockSurveySearchRepository;

}
