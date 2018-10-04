package ai.preferred.crawler.example.master;

import ai.preferred.crawler.example.entity.Car;
import ai.preferred.crawler.example.entity.Listing;
import ai.preferred.venom.response.VResponse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ween Jiann Lee
 */
public class ListingParser {

    public static class FinalResult {
        //Declaration of object variables
        private final List<Car> cars;
        private final String nextPage;
        
        //Constructor
        private FinalResult(List<Car> cars, String nextPage) {
            this.cars = cars;
            this.nextPage = nextPage;
        }
        
        //Object Methods
        public List<Car> getListings() {
            return cars;
        }

        public String getNextPage() {
            return nextPage;
        }
    } // End of FinalResult Class
    
    //Obtain list of cars and url for next page
    public static FinalResult parse(VResponse response) {
        final Document document = response.getJsoup();
        return new FinalResult(
                parseCars(document),
                parseNextPage(document)
        );
    }
    //Return a list of cars parsef from the results page
    private static List<Car> parseCars(Document document) {
        final ArrayList<Car> jobList = new ArrayList<>();
        
        //Selector to select all rows of cars
        final String sel = "#contentblank > table > tbody > tr > td > table > tbody > tr > td > table > tbody > tr > td > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(2) > div > strong";
        final Elements cars = document.select(sel);
        
        //For each car row found, extract the url and car make+model
        for (Element car : cars) {

            final Element a = car.getElementsByTag("a").first();
            final String url = a.attr("abs:href");
            final String name = a.text();
//            final String company = "Test";

            final Car carObj = new Car(name, url);
//      final Ca listing = new Listing(url, name, company);

            jobList.add(carObj);
        }

        return jobList; //List of all car listings found to be crawled
    }
    
    //Find the next results page to crawl
    public static String parseNextPage(Document document) {
        final String sel2 = "#contentblank > div:nth-child(4) > div:nth-child(4) > a.pagebar";
//    final Element nextPage = document.select("a.prev-next.test-pagination-next").first();
//    if (nextPage == null) {
//      return null;
//    }

        final Elements Pages = document.select(sel2);
        Element e = null;
        
        if (Pages.size() == 1) {
            if (Pages.get(0).text().equals("Next ")){
                e = Pages.get(0);
            }
            else{
                return null;
            }

        } else {
            e = Pages.get(1);
        }

        return e.attr("abs:href");
    }

}
