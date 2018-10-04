package ai.preferred.crawler.example.master;

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

    private final List<Listing> listings;

    private final String nextPage;

    private FinalResult(List<Listing> listings, String nextPage) {
      this.listings = listings;
      this.nextPage = nextPage;
    }

    public List<Listing> getListings() {
      return listings;
    }

    public String getNextPage() {
      return nextPage;
    }
  }

  public static FinalResult parse(VResponse response) {
    final Document document = response.getJsoup();
    return new FinalResult(
        parseListings(document),
        parseNextPage(document)
    );
  }

  private static List<Listing> parseListings(Document document) {
    final ArrayList<Listing> jobList = new ArrayList<>();

    //TEST Commenting - Original
    
//    final Elements jobs = document.select("div.listResults div.-item");
//
//    for (Element job : jobs) {
//      final Element title = job.select("div.-job-summary > div.-title > h2 > a").first();
//      final String name = title.text();
//      final String url = title.attr("abs:href");
//
//      final String company = job.select("div.fc-black-700 span:nth-of-type(1)").first().text();
//
//      final Listing listing = new Listing(url, name, company);
//
//      jobList.add(listing);
//    }

    final String sel = "#contentblank > table > tbody > tr > td > table > tbody > tr > td > table > tbody > tr > td > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(2) > div > strong";
    final Elements cars = document.select(sel);
    for (Element car : cars) {
       
      final Element a = car.getElementsByTag("a").first();
      final String url = a.attr("abs:href");
      final String name = a.text();
      final String company = "Test";
      
      final Listing listing = new Listing(url, name, company);
      

      jobList.add(listing);
    }

    return jobList;
  }

  public static String parseNextPage(Document document) {
    final String sel2 = "#contentblank > div:nth-child(4) > div:nth-child(4) > a.pagebar";
//    final Element nextPage = document.select("a.prev-next.test-pagination-next").first();
//    if (nextPage == null) {
//      return null;
//    }
    
    final Elements Pages = document.select(sel2);
    boolean first = true;
    Element e = null;
    
    if(Pages.size()==1){
        if(first){
            e = Pages.get(0);
            first=false;
        } else {
            return null;
        }
          
    } else {
        e = Pages.get(1);
    }
    
    return e.attr("abs:href");
  }

}
