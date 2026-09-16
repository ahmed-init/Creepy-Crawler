package com.webcrawler.demo.model;

public class CrawlInfo {

    private String url;
    private int statusCode;
    private String crawledAt;

    public CrawlInfo() {

    }

    public CrawlInfo(String url, int statusCode, String crawledAt) {
        this.url = url;
        this.statusCode = statusCode;
        this.crawledAt = crawledAt;
    }

    public String getUrl() {
        return url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getCrawledAt() {
        return crawledAt;
    }
}