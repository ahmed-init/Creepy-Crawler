package com.webcrawler.demo.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CrawlerService {

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
            System.out.print("Crawling:" + currenturl);
            visitedurls.add(currenturl);
            try {
                Document document = Jsoup.connect(currenturl)
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();
                crawledones.add(currenturl);

                for (Element link : document.select("a[href]")) {

                    String discoveredUrl = link.absUrl("href");
                    if (!discoveredUrl.isEmpty() && !visitedurls.contains(discoveredUrl)
                            && !crawledones.contains(discoveredUrl)) {
                        discoveredUrls.add(discoveredUrl);
                        qurls.offer(discoveredUrl);
                    }
                }

            } catch (Exception e) {
                System.out.println("Error while crawling: " + e.getMessage());
            }

        }

        return crawledones;
    }
}