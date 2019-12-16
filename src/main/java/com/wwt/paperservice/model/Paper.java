package com.wwt.paperservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

class Venue {
    String id;
    String raw;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}

@Document("Paper")
public class Paper implements Serializable {

    private static final long serialVersionUID = -171669790372885614L;

    @Id
    @Field("_id")
    private String id;

    @Field("title")
    private String title;

    @Field("authors")
    private Author[] authors;

    @Field("venue")
    private Venue venue;

    @Field("year")
    private int year;

    @Field("keywords")
    private String[] keywords;

    @Field("n_citation")
    private int n_citation; // 论文被引用数量

    @Field("page_start")
    private String page_start;

    @Field("page_end")
    private String page_end;

    @Field("doc_type")
    private String type; // 论文类型,是书籍,还是论文,还是其他

    @Field("lang")
    private String language;

    @Field("publisher")
    private String publisher;

    @Field("volume")
    private String volume;

    @Field("issue")
    private String issue;

    @Field("issn")
    private String issn;

    @Field("isbn")
    private String isbn;

    @Field("doi")
    private String doi;

    @Field("pdf")
    private String pdf;

    @Field("url")
    private String[] urls; // 下载链接可能有很多个,所以这里用了数组

    @Field("abstract")
    private String summary;

    @Field("price")
    private int price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public int getN_citation() {
        return n_citation;
    }

    public void setN_citation(int n_citation) {
        this.n_citation = n_citation;
    }

    public String getPage_start() {
        return page_start;
    }

    public void setPage_start(String page_start) {
        this.page_start = page_start;
    }

    public String getPage_end() {
        return page_end;
    }

    public void setPage_end(String page_end) {
        this.page_end = page_end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
