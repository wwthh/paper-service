package com.wwt.paperservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wwt.paperservice.model.Author;
import com.wwt.paperservice.model.Paper;
import com.wwt.paperservice.model.PaperRepository;
import com.wwt.paperservice.model.PapersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PaperController {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PapersRepository papersRepository= new PapersRepository();

    @PostMapping("/papers")
    public Map<String, Object> insertPaper(@RequestBody String requestbody) {
        System.out.println("insertPaper");
        Map<String, Object> params = new HashMap<>();
        try {
            JSONObject obj = JSON.parseObject(requestbody);
            Paper paper = JSON.toJavaObject(obj, Paper.class);
            paperRepository.insert(paper);
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
    public Map<String, Object> getPaperById(@PathVariable(required = true) String _id,
                                            @RequestParam(name = "token", required = false) String token) {
        System.out.println("GetPaperById:" + _id);
        Map<String, Object> params = new HashMap<>();

        try {
            Optional<Paper> Opaper = paperRepository.findById(_id);
            if (!Opaper.isPresent()) {
                params.put("success", false);
                Map<String, Integer> content = new HashMap<>();
                content.put("error_code", 1);
                params.put("content", content);
            } else {
                Paper paper = Opaper.get();
                params.put("success", true);
                System.out.println(paper.toString());
                params.put("content", paper);
            }
        } catch (Exception e) {
            params.put("success", false);
            Map<String, Integer> content = new HashMap<>();
            content.put("error_code", 0);
            params.put("content", content);
        }

        return params;
    }

    // GET http://ip:port/v1/papers/?request_params
    // 返回部分字段
    @GetMapping("/papers")
    public Map<String, Object> getPaperByPage(@RequestParam(name = "size", required = true) int size,
                                              @RequestParam(name = "page", required = true) int page,
                                              @RequestParam(name = "domain", required = false) String domain,
                                              @RequestParam(name = "key", required = true) String key,
                                              @RequestParam(name = "sort", required = false) String sort,
                                              @RequestParam(name = "direction", required = false) boolean direction,
                                              @RequestParam(name = "free", required = false) boolean free) {
        System.out.println("getPaperByPage");
        Map<String, Object> params = new HashMap<>();
        try {
            if (domain == null)
                domain = "title";
            if (sort == null)
                sort = "year";

            Sort.Order order = new Sort.Order(Sort.Direction.DESC, sort);
            if (!direction)
                order = new Sort.Order(Sort.Direction.ASC, sort);
            PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(order));
            int count = 0;
            List<Paper> papers = null;
            switch (domain) {
                case "title":
                    papers = paperRepository.findByTitleLike(key, pageable).getContent();
                    count = paperRepository.countByTitleLike(key);
                    break;
                case "keyword":
                    String[] str1 = { key };
                    papers = paperRepository.findByKeywordsLike(str1, pageable).getContent();
                    count = paperRepository.countByKeywordsLike(key);
                    break;
                case "author":
                    String[] str2 = { key };
                    papers = papersRepository.findByAuthorName(key,pageable).getContent();
//                    Query query = new Query(Criteria.where("authors.name").is(key));
//                    papers = ;
                    count = papersRepository.getCount();
                    break;
                case "summary":
                    papers = paperRepository.findBySummaryLike(key,pageable).getContent();
                    count = paperRepository.countBySummaryLike(key);
                    break;
                default:
                    params.put("success", false);
                    Map<String, Integer> content = new HashMap<>();
                    content.put("error_code", 2);
                    params.put("content", content);
                    return params;
            }

            if (papers.isEmpty()) {
                System.out.println("Get No Answer ");
                params.put("success", false);
                Map<String, Integer> content = new HashMap<>();
                content.put("error_code", 1);
                params.put("content", content);
            } else {
                System.out.println("Get! " + papers);
                params.put("success", true);
                Map<String, Object> content = new HashMap<>();
                content.put("total", count);
                List<JSONObject> list = new LinkedList<JSONObject>();
                papers.forEach(value -> {
                    JSONObject re = new JSONObject();

                    re.put("title", value.getTitle());

                    if (value.getAuthors() != null)
                        re.put("authors", value.getAuthors());
                    else
                        re.put("authors", null);

                    if (value.getType() != null)
                        re.put("doc_type", value.getType());
                    else
                        re.put("doc_type", null);

                    if (value.getVolume() != null)
                        re.put("volume", value.getVolume());
                    else
                        re.put("volume", null);

                    if (value.getIssue() != null)//issue:期号
                        re.put("issue", value.getIssue());
                    else
                        re.put("issue", null);

                    if (value.getPage_start() != null)
                        re.put("page_start", value.getPage_start());
                    else
                        re.put("page_start", null);

                    if (value.getPage_end() != null)
                        re.put("page_end", value.getPage_end());
                    else
                        re.put("page_end", null);

                    re.put("year", value.getYear());
                    list.add(re);

                    // String str = "{" +
                    // "\"title\":\"" + value.getTitle() + "\"," ;
                    // if (value.getAuthors()!=null){
                    // str += "\"author\":\"" + value.getAuthors()[0].getName()+ "\"," ;
                    // }else{
                    // str += "\"author\":\"" + null + "\"," ;
                    // }
                    //
                    // str += "\"year\":\"" + value.getYear() + "\"," ;
                    //
                    // str += "}";
                    // System.out.println(str);
                    // list.add(JSONObject.parseObject(str));
                });
                content.put("papers", list);
                params.put("content", content);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.getMessage());
            params.put("success", false);
            Map<String, Integer> content = new HashMap<>();
            content.put("error_code", 0);
            params.put("content", content);
        }

        return params;
    }
}
