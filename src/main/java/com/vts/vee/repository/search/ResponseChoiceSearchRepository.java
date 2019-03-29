package com.vts.vee.repository.search;

import com.vts.vee.domain.ResponseChoice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ResponseChoice entity.
 */
public interface ResponseChoiceSearchRepository extends ElasticsearchRepository<ResponseChoice, Long> {
}
