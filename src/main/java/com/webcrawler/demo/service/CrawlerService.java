package com.webcrawler.demo.service;

import com.webcrawler.demo.model.CrawlInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CrawlerService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    CrawlerService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private boolean isValidUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public Set<String> crawl(String url) {

        List<String> discoveredUrls = new ArrayList<>();
        Queue<String> qurls = new LinkedList<>();
        Set<String> visitedurls = new HashSet<>();
        Set<String> crawledones = new HashSet<>();
        qurls.add(url);
        while (!qurls.isEmpty()) {

            String currenturl = qurls.poll();
            if (crawledones.contains(currenturl)) {
                continue;
            }
            System.out.println("Crawling:" + currenturl);
            visitedurls.add(currenturl);
            try {
                Document document = Jsoup.connect(currenturl)
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();

                // store the visited url in redis
                redisTemplate.opsForValue().set(currenturl, "visited");
                crawledones.add(currenturl);
                System.out.println("Added to crawledones: " + currenturl);
                // Create crawl information
                CrawlInfo crawlInfo = new CrawlInfo(
                        currenturl,
                        200,
                        LocalDateTime.now().toString());
                // Convert crawl information to JSON
                String crawlInfoJson = objectMapper.writeValueAsString(crawlInfo);

                // Store crawl information in Redis
                redisTemplate.opsForValue().set(
                        "crawl:" + currenturl,
                        crawlInfoJson);

                for (Element link : document.select("a[href]")) {

                    String discoveredUrl = link.absUrl("href");
                    if (isValidUrl(discoveredUrl) && !discoveredUrl.isEmpty() && !visitedurls.contains(discoveredUrl)
                            && !crawledones.contains(discoveredUrl)) {
                        discoveredUrls.add(discoveredUrl);
                        qurls.offer(discoveredUrl);
                    }
                }

            } catch (Exception e) {
                System.out.println("Error while crawling: "
                        + currenturl
                        + " : "
                        + e.getMessage());
            }

        }

        return crawledones;
    }
}