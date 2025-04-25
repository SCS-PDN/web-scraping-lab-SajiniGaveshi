import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class HeadingScraper {


    //headings
    public static List<String> getHeadingsWithForLoop(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        List<String> headingsList = new ArrayList<>();

        // Select all heading elements (h1 to h6)
        Elements headingElements = doc.select("h1, h2, h3, h4, h5, h6");

        // Iterate through the Elements using a for loop
        for (int i = 0; i < headingElements.size(); i++) {
            Element heading = headingElements.get(i);
            headingsList.add(heading.text().trim());
        }

        return headingsList;
    }

    public static void main(String[] args) {
        String bbcNewsUrl = "https://www.bbc.com/news";

        try {
            List<String> headings = getHeadingsWithForLoop(bbcNewsUrl);
            if (headings != null && !headings.isEmpty()) {
                System.out.println("Headings on " + bbcNewsUrl + ":");
                for (String heading : headings) {
                    System.out.println(heading);
                }
            } else {
                System.out.println("No headings found on " + bbcNewsUrl + ".");
            }
        } catch (IOException e) {
            System.err.println("Error scraping headings: " + e.getMessage());
        }
    }

    //Authors

}