package com.webcrawler.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webcrawler.demo.service.CrawlerService;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CrawlerController {
    private CrawlerService service;

    public CrawlerController(CrawlerService crawlerService) {
        this.service = crawlerService;
    }

    @GetMapping("/crawl")
    public Set<String> docrawl(String url) {
        return service.crawl(url);
    }

    @GetMapping("/check")
    public String getMethodName() {
        return "Web crawler api is working";
    }

}
