/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.preferred.crawler.example.master;


import ai.preferred.crawler.example.EntityCSVStorage;
import ai.preferred.crawler.example.entity.Car;
import ai.preferred.crawler.example.entity.Listing;
import ai.preferred.venom.Handler;
import ai.preferred.venom.Session;
import ai.preferred.venom.Worker;
import ai.preferred.venom.job.Scheduler;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.response.VResponse;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author KuroiKuro
 */
public class carHandler implements Handler {

    private static final Logger LOGGER = LoggerFactory.getLogger(carHandler.class);

    @Override
    public void handle(Request request, VResponse response, Scheduler scheduler, Session session, Worker worker) {
        
        /*
        This method handles the crawling of each individual car listing found.
        Within each page, the data is parsed and added to a car object.
        After parsing, append the Car object for writing to csv file.
        */
        
        LOGGER.info("Processing carHandler {}", request.getUrl());
        //Get HTML String and Jsoup
        final String html = response.getHtml();
        final Document document = response.getJsoup();

        // Get the job listing array list we created
        final EntityCSVStorage csvStorage = session.get(ListingCrawler.CSV_STORAGE_KEY);
//        final ArrayList<Car> jobListing = session.get(ListingCrawler.JOB_LIST_KEY);

        //Initialise ArrayList and Car
        ArrayList<Car> carList = new ArrayList<Car>();
        Car car = new Car();

        //Define selector to get all rows of data
        String sel = "#main_left > div:nth-child(2) > table > tbody > tr";

        //Get and store URL and car name
        String url = request.getUrl();
        String name = document.select("#toMap > div").first().text();

        //Set the name and url to the car object
        car.setName(name);
        car.setUrl(url);

        //Select all the elements containing data
        Elements elements = document.select(sel);

        String others = "";

        //For each line of data, parse data
        for (Element e : elements) {
            //Check that the first data row selected is not null before executing the rest of code
            if (e.select("td.label > strong").first() != null) {
                String key = e.select("td.label > strong").first().text();
                String value = e.select("td:nth-child(2)").first().text();
                
                //Assign each scraped data to the correct data field in car
                switch (key) {
                    case "Price":
                        car.setPrice(value);
                        break;
                    case "Depreciation":
                        car.setDepreciation(value);
                        break;
                    case "Reg Date":
                        car.setRegDate(value);
                        break;
                    case "Manufactured":
                        car.setManufactured(value);
                        break;
                    case "Mileage":
                        car.setMileage(value);
                        break;
                    case "Transmission":
                        car.setTransmission(value);
                        break;
                    case "Engine Cap":
                        car.setEngineCap(value);
                        break;
                    case "Road Tax":
                        car.setRoadTax(value);
                        break;
                    case "Power":
                        car.setPower(value);
                        break;
                    case "Curb Weight":
                        car.setCurbWeight(value);
                        break;
                    case "Features":
                        car.setFeatures(value);
                        break;
                    case "Accessories":
                        car.setAccessories(value);
                        break;
                    case "Description":
                        car.setDescription(value);
                        break;
                    case "COE":
                        car.setCoe(value);
                        break;
                    case "OMV":
                        car.setOmv(value);
                        break;
                    case "ARF":
                        car.setArf(value);
                        break;
                    case "Dereg Value":
                        car.setDeregValue(value);
                        break;
                    case "No. Of Owners":
                        car.setNoOfOwners(value);
                        break;
                    case "Type of Veh":
                        car.setTypeOfVeh(value);
                        break;
                    case "Category":
                        car.setCategory(value);
                        break;
                    default:
                        //Catch any categories not covered by above cases
                        others += key + ":" + value + ";";
                        break;
                } //End of switch

            } // End of if

        }//End of loop
        
        car.setOthers(others);
        //Append the car object for writing to the csv file
        csvStorage.append(car);

    }
}
