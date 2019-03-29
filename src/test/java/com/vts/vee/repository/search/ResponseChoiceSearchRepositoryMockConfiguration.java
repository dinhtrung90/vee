package com.vts.vee.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ResponseChoiceSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ResponseChoiceSearchRepositoryMockConfiguration {

    @MockBean
    private ResponseChoiceSearchRepository mockResponseChoiceSearchRepository;

}
