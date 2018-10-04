/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.preferred.crawler.example.master;

import ai.preferred.venom.Session;
import ai.preferred.venom.Worker;
import ai.preferred.venom.job.Scheduler;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.response.VResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.preferred.crawler.example.EntityCSVStorage;
import ai.preferred.crawler.example.entity.Car;
import ai.preferred.crawler.example.entity.Listing;
import ai.preferred.venom.Handler;
import ai.preferred.venom.Session;
import ai.preferred.venom.Worker;
import ai.preferred.venom.job.Scheduler;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.request.VRequest;
import ai.preferred.venom.response.VResponse;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 *
 * @author ronse
 */
public class carHandler implements Handler{
    private static final Logger LOGGER = LoggerFactory.getLogger(carHandler.class);

  @Override
  public void handle(Request request, VResponse response, Scheduler scheduler, Session session, Worker worker){
      LOGGER.info("Processing carHandler {}", request.getUrl());
      //Get HTML String and Jsoup
      final String html = response.getHtml();
      final Document document = response.getJsoup();
      
      // Get the job listing array list we created
      final EntityCSVStorage csvStorage = session.get(ListingCrawler.CSV_STORAGE_KEY);
      final ArrayList<Listing> jobListing = session.get(ListingCrawler.JOB_LIST_KEY);
      
      ArrayList<Car> carList = new ArrayList<Car>();
      String sel = "#main_left > div:nth-child(2) > table > tbody > tr";
      String url = request.getUrl();
      //Select all the elements containing data
//      for (Listing listing:jobListing){
//          String cName = listing.getName();
//          String url = listing.getUrl();
//          Car car = new Car(cName, url);
//          
//          carList.add(car);
//      }
      
      Elements elements = document.select(sel);
      Car car = new Car();
      String others = "";
      for (Element e:elements){
          if (e.select("td.label > strong").first() != null){
              String key = e.select("td.label > strong").first().text();
          String value = e.select("td:nth-child(2)").first().text();
          
              switch (key){
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
              case "Type Of Veh":
                  car.setTypeOfOVeh(value);
                  break;
              case "Category":
                  car.setCategory(value);
                  break;
              default:
                  others += key + ":" + value + ";";
                  
                  break;
          }
              
          }
          
          
          
      }//End of loop
      car.setOthers(others);
      csvStorage.append(car);
      
  }
}
