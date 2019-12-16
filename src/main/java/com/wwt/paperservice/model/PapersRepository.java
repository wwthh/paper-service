package com.wwt.paperservice.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.regex.Pattern;

@Repository("paper")
public class PapersRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    int count=0;

    public int getCount() {
        return count;
    }

    public Page<Paper> findByAuthorName(String key, Pageable pageable){
        Query query = new Query();
        // 下面两句是模糊搜索
//        Pattern pt= Pattern.compile("^.*" + key + ".*$");
//        query.addCriteria(Criteria.where("authors").exists(true).and("authors.name").regex(pt));

        // 下面一句是精确搜索
        query.addCriteria(Criteria.where("authors").exists(true).and("authors.name").is(key));

//        下面两行是分页坐在mongo里的写法
//        this.count = (int) mongoTemplate.count(query, Paper.class);
//        query.with(pageable);

        List<Paper> list = mongoTemplate.find(query,Paper.class);   //search

        // 下面三页是我写分页
        this.count = list.size();
        int start = pageable.getPageNumber()*pageable.getPageSize();
        list = list.subList(start, Math.min(start + pageable.getPageSize(), count));

        return new PageImpl<Paper>(list,pageable,count);
    }

}
