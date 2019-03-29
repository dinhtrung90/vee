package com.vts.vee.repository.search;

import com.vts.vee.domain.SurveyResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SurveyResponse entity.
 */
public interface SurveyResponseSearchRepository extends ElasticsearchRepository<SurveyResponse, Long> {
}
