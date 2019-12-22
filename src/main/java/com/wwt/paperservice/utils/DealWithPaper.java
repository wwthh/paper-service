package com.wwt.paperservice.utils;

import com.alibaba.fastjson.JSONObject;
import com.wwt.paperservice.model.Author;
import com.wwt.paperservice.model.Paper;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class DealWithPaper {

    public static void main(Paper paper) {
        Author[] authors = paper.getAuthors();
        if (authors.length > 100)
            authors = Arrays.copyOfRange(authors, 0, 100);
        for (Author author : authors) {
            try {
                new RestTemplate().getForObject("http://localhost:9200/expert3/expert3/" + author.getId(),
                        JSONObject.class);
            } catch (Exception e) {
                author.setId(null);
            }
        }
        paper.setAuthors(authors);
    }
}
