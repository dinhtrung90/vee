package com.vts.vee.repository.search;

import com.vts.vee.domain.QuestionType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the QuestionType entity.
 */
public interface QuestionTypeSearchRepository extends ElasticsearchRepository<QuestionType, Long> {
}
