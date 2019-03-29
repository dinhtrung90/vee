package com.vts.vee.repository.search;

import com.vts.vee.domain.QuestionOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the QuestionOrder entity.
 */
public interface QuestionOrderSearchRepository extends ElasticsearchRepository<QuestionOrder, Long> {
}
