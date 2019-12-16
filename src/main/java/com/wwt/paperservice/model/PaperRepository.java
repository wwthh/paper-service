package com.wwt.paperservice.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PaperRepository extends MongoRepository<Paper, String> {

    Page<Paper> findByTitleLike(String key, Pageable pageable);

    Page<Paper> findByAuthorsLike(String[] key, Pageable pageable);

    Page<Paper> findByKeywordsLike(String[] keyword, Pageable pageable);

    Page<Paper> findBySummaryLike(String key, Pageable pageable);



    int countByTitleLike(String key);

    int countByKeywordsLike(String key);

    int countByAuthorsLike(String key);

    int countBySummaryLike(String key);

}
