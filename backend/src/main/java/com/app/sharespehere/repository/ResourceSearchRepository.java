package com.app.sharespehere.repository;

import com.app.sharespehere.model.ResourceSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceSearchRepository extends ElasticsearchRepository<ResourceSearch,Long> {
}
