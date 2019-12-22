package com.wwt.paperservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wwt.paperservice.model.BigPaper;
import com.wwt.paperservice.model.Paper;
import com.wwt.paperservice.model.PaperRepository;
import com.wwt.paperservice.utils.DealWithPaper;
import com.wwt.paperservice.utils.Error;
import com.wwt.paperservice.utils.Success;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class PaperController {

    @Autowired
    private PaperRepository paperRepository;

    @PostMapping("/papers")
    public Map<String, Object> insertPaper(@RequestBody String requestbody) {
        System.out.println("insertPaper");
        Map<String, Object> params = new HashMap<>();
        try {
            JSONObject obj = JSON.parseObject(requestbody);
            obj.put("summary", obj.getString("abstract"));
            obj.put("abstract", null);
            Paper paper = JSON.toJavaObject(obj, Paper.class);
            paperRepository.index(paper);
            params.put("success", true);
            params.put("content", paper);
        } catch (Exception e) {
            System.out.println("ERROR" + e.getMessage());
            params.put("success", false);
            Map<String, Integer> content = new HashMap<>();
            content.put("error_code", 0);
            params.put("content", content);
        }
        return params;
    }

    // GET http://ip:port/v1/papers/<id>/
    // 返回全部字段
    @GetMapping("/papers/{_id}")
    public Map<String, Object> getPaperById(@PathVariable(name = "_id") String _id) {
        System.out.println("GetPaperById:" + _id);
        try {
            Paper paper = paperRepository.findById(_id).get();
            DealWithPaper.main(paper);
            JSONObject obj = JSONObject.parseObject(JSONObject.toJSONString(paper));
            obj.put("abstract", paper.getSummary());
            obj.put("summary", null);
            return Success.successResponse(obj);
        } catch (Exception e) {
            return Error.errorResponse(1);
        }
    }

    // GET http://ip:port/v1/papers/?request_params
    // 返回部分字段
    @GetMapping("/papers")
    public Map<String, Object> getPaperByPage(@RequestParam(name = "size", required = true) int size,
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "domain", required = false) String domain,
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "direction", required = false) Boolean direction,
            @RequestParam(name = "free", required = false) boolean free) {
        System.out.println("getPaperByPage");
        Map<String, Object> params = new HashMap<>();
        try {
            String queryString;
            String orderString;
            if(key==null) key = "";
            if (domain == null) {
                String fullQueryString = "\"query\": {\n" + "    \"bool\": {\n" + "      \"should\": [\n"
                        + "        { \"match_phrase\": { \"title\": \"%s\"   }},\n"
                        + "        { \"match_phrase\": { \"keywords\": \"%s\"   }},\n"
                        + "        { \"match_phrase\": { \"summary\": \"%s\"   }}\n" + "      ]\n" + "    \n" + "}\n" + "}";
                queryString = String.format(fullQueryString, key, key, key);
            } else {
                if (domain.equals("author") || domain.equals("authors"))
                    domain = "authors.name";
                else if (domain.equals("abstract"))
                    domain = "summary";
                String singleQueryString = "\"query\": {\n         \"match_phrase\": { \"%s\": \"%s\" }\n  }";
                queryString = String.format(singleQueryString, domain, key);
                if(domain.equals("eid"))
                    queryString = String.format("\"query\": {\n         \"term\": { \"authors.id\": \"%s\" }\n  }", key);
            }
            if (sort == null || sort.equals("")) {
                sort = "_score";
            }
            String bigOrderString = "\"sort\": {\"%s\":   { \"order\": \"%s\" }}";
            if (direction == null || direction)
                orderString = String.format(bigOrderString, sort, "DESC");
            else
                orderString = String.format(bigOrderString, sort, "ASC");

            String queryBody;
            if (orderString.equals(""))
                queryBody = "{" + queryString + "}";
            else if(key.equals("")) {
                queryBody = "{" + orderString + "}";
            } else queryBody = "{" + queryString + "," + orderString + "}";
            int from = (page - 1) * size;
            System.out.println(queryBody);
            String urlString = "http://localhost:9200/paper3/paper3/_search?size=%d&from=%d";
            String url = String.format(urlString, size, from);
            System.out.println(url);
            RestTemplate restTemplate = new RestTemplate();
            String responseBody = restTemplate.postForObject(url, JSONObject.parseObject(queryBody), String.class);
            JSONObject response = JSONObject.parseObject(responseBody);

            if (response.getString("timed_out").equals("true"))
                return Error.errorResponse(1);

            JSONObject firstHits = response.getJSONObject("hits");
            JSONObject totalObject = firstHits.getJSONObject("total");
            int count = totalObject.getInteger("value");
            List<BigPaper> bigPapers = JSONObject.parseArray(firstHits.getString("hits"), BigPaper.class);
            List<Paper> papers = new ArrayList<Paper>();
            for (BigPaper bigPaper : bigPapers) {
                papers.add(bigPaper.get_source());
            }

            if (papers.isEmpty()) {
                return Error.errorResponse(1);
            } else {
                params.put("success", true);
                Map<String, Object> content = new HashMap<>();
                content.put("total", count);
                List<JSONObject> list = new LinkedList<JSONObject>();
                papers.forEach(value -> {
                    DealWithPaper.main(value);
                    JSONObject re = JSONObject.parseObject(JSONObject.toJSONString(value));
                    re.put("abstract", re.getString("summary"));
                    re.put("summary", null);
                    list.add(re);
                });
                content.put("papers", list);
                params.put("content", content);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Error.errorResponse(0);
        }
        return params;
    }
}
