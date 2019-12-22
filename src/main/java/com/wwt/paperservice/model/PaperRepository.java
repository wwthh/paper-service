package com.wwt.paperservice.model;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PaperRepository extends ElasticsearchRepository<Paper, String> {
}
