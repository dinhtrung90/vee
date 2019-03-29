package com.vts.vee.repository.search;

import com.vts.vee.domain.VeeResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the VeeResponse entity.
 */
public interface VeeResponseSearchRepository extends ElasticsearchRepository<VeeResponse, Long> {
}
