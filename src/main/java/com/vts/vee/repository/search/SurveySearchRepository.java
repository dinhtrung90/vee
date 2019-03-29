package com.vts.vee.repository.search;

import com.vts.vee.domain.Survey;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Survey entity.
 */
public interface SurveySearchRepository extends ElasticsearchRepository<Survey, Long> {
}
