import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateScraper {

    public static List<Date> getArticleDates(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        List<Date> articleDates = new ArrayList<>();

        // **IMPORTANT:** Inspect the BBC News HTML to find the correct selector for article dates.
        // The selector below is based on previous observations and might need updating.
        Elements dateElements = doc.select(".gs-o-bullet__text span");

        for (Element dateElement : dateElements) {
            String dateText = dateElement.text().trim();
            Date parsedDate = parseBBCDate(dateText);
            if (parsedDate != null) {
                articleDates.add(parsedDate);
            }
        }

        // Also try to find <time> tags with datetime attribute (more reliable)
        Elements timeElements = doc.select("time[datetime]");
        for (Element time : timeElements) {
            String datetime = time.attr("datetime");
            try {
                Date parsedDateTime = javax.xml.bined.DatatypeConverter.parseDateTime(datetime).getTime();
                articleDates.add(parsedDateTime);
            } catch (IllegalArgumentException e) {
                System.err.println("Could not parse datetime: " + datetime);
            }
        }

        return articleDates;
    }

    private static Date parseBBCDate(String dateString) {
        // Add more formats if BBC News uses different date formats
        String[] formats = {
                "dd MMMM yyyy", // e.g., 25 April 2025
                "dd MMM yyyy",  // e.g., 25 Apr 2025
                "h:mma dd MMM yyyy", // e.g., 3:43pm 25 Apr 2025
                "h:mma 'GMT' dd MMM yyyy", // e.g., 3:43pm GMT 25 Apr 2025
                "h:mma 'BST' dd MMM yyyy"  // e.g., 3:43pm BST 25 Apr 2025 (if applicable)
        };

        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                return sdf.parse(dateString);
            } catch (ParseException e) {
                // Try the next format
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String bbcNewsUrl = "https://www.bbc.com/news";

        try {
            List<Date> dates = getArticleDates(bbcNewsUrl);
            if (dates != null && !dates.isEmpty()) {
                System.out.println("Article Dates on " + bbcNewsUrl + ":");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                for (Date date : dates) {
                    System.out.println(outputFormat.format(date));
                }
            } else {
                System.out.println("No article dates found on " + bbcNewsUrl + ". Please check the URL and CSS selectors.");
            }
        } catch (IOException e) {
            System.err.println("Error scraping dates: " + e.getMessage());
        }
    }
}