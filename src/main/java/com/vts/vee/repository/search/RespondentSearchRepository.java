package com.vts.vee.repository.search;

import com.vts.vee.domain.Respondent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Respondent entity.
 */
public interface RespondentSearchRepository extends ElasticsearchRepository<Respondent, Long> {
}
