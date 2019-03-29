package com.vts.vee.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of VeeResponseSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class VeeResponseSearchRepositoryMockConfiguration {

    @MockBean
    private VeeResponseSearchRepository mockVeeResponseSearchRepository;

}
