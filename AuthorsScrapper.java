package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AuthorScraperMethod4 {

    public static Set<String> getAuthorsNearHeadlinesWithLoop(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Set<String> authors = new HashSet<>();

        Elements articleContainers = doc.select(".gs-c-promo"); // Adjust if needed

        for (int i = 0; i < articleContainers.size(); i++) {
            Element container = articleContainers.get(i);
            Element headlineElement = container.selectFirst(".gs-c-promo-headline__text"); // Adjust if needed

            if (headlineElement != null) {
                Element precedingElement = headlineElement.previousElementSibling();
                if (precedingElement != null) {
                    String text = precedingElement.text().trim();
                    Pattern authorPattern = Pattern.compile("^(By|Written by)\\s+([A-Za-z\\s\\.-]+)", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = authorPattern.matcher(text);
                    if (matcher.find()) {
                        authors.add(matcher.group(2).trim());
                    }
                }
                // Also check the next sibling
                Element followingElement = headlineElement.nextElementSibling();
                if (followingElement != null) {
                    String text = followingElement.text().trim();
                    Pattern authorPattern = Pattern.compile("^(By|Written by)\\s+([A-Za-z\\s\\.-]+)", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = authorPattern.matcher(text);
                    if (matcher.find()) {
                        authors.add(matcher.group(2).trim());
                    }
                }
            }
        }

        return authors;
    }

    public static void main(String[] args) {
        String bbcNewsUrl = "https://www.bbc.com/news";

        try {
            Set<String> authors = getAuthorsNearHeadlinesWithLoop(bbcNewsUrl);
            if (authors != null && !authors.isEmpty()) {
                System.out.println("Authors found near headlines on " + bbcNewsUrl + ":");
                for (String author : authors) {
                    System.out.println(author);
                }
            } else {
                System.out.println("No authors found near headlines on " + bbcNewsUrl + " using this method. Inspect the page source.");
            }
        } catch (IOException e) {
            System.err.println("Error scraping authors: " + e.getMessage());
        }
    }
}