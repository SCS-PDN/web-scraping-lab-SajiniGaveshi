/*package com.example; // Adjust package name as needed

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

class TitleScraper {

    public static void main(String[] args) {
        String newsPageUrl = "https://www.bbc.com/news"; // BBC News homepage URL

        try {
            String title = scrapeTitle(newsPageUrl);
            if (title != null) {
                System.out.println("Title of BBC News page: " + title);
            } else {
                System.out.println("Could not scrape the title.");
            }
        } catch (IOException e) {
            System.err.println("Error scraping the title: " + e.getMessage());
        }
    }

    public static String scrapeTitle(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.title();
    }

}
 */

package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BBCNewsScraper {

    private String url;
    private String title;
    private List<String> links;

    public BBCNewsScraper(String url) {
        this.url = url;
        this.links = new ArrayList<>();
    }

    public void scrapeData() throws IOException {
        Document doc = Jsoup.connect(url).get();
        this.title = doc.title();
        Elements linkElements = doc.select("a[href]");
        for (Element link : linkElements) {
            this.links.add(link.attr("abs:href"));
        }
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLinks() {
        return links;
    }

    public static void main(String[] args) {
        String bbcNewsUrl = "https://www.bbc.com/news";
        BBCNewsScraper scraper = new BBCNewsScraper(bbcNewsUrl);

        try {
            scraper.scrapeData();
            System.out.println("Title of the page: " + scraper.getTitle());
            System.out.println("\nAll links on the page:");
            for (String link : scraper.getLinks()) {
                System.out.println(link);
            }
        } catch (IOException e) {
            System.err.println("Error scraping BBC News page: " + e.getMessage());
        }
    }
}

