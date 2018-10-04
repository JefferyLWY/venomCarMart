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
import ai.preferred.venom.job.Priority;
import ai.preferred.venom.job.Scheduler;
import ai.preferred.venom.request.Request;
import ai.preferred.venom.request.VRequest;
import ai.preferred.venom.response.VResponse;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author Ween Jiann Lee
 */
public class ListingHandler implements Handler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListingHandler.class);

    @Override
    public void handle(Request request, VResponse response, Scheduler scheduler, Session session, Worker worker) {
        LOGGER.info("Processing {}", request.getUrl());

        // Get the job listing array list we created
        final ArrayList<Car> jobListing = session.get(ListingCrawler.JOB_LIST_KEY);

        // Get the job listing array list we created
        final EntityCSVStorage csvStorage = session.get(ListingCrawler.CSV_STORAGE_KEY);

        // Get HTML
        final String html = response.getHtml();

        // JSoup
        final Document document = response.getJsoup();

        // We will use a parser class
        final ListingParser.FinalResult finalResult = ListingParser.parse(response); //finalresult contains a list of car listings and a url for the next results page
        int i = 0;

        for (Car carObj : finalResult.getListings()) { //Loop through each car result in page
            //Each carObj contains car make+model and listing URL
            LOGGER.info("Found {} car: {} [{}]", Integer.toString(++i), carObj.getName(), carObj.getUrl());
            //Add a new job to the scheduler for each car found
            scheduler.add(new VRequest(carObj.getUrl()), new carHandler(), Priority.HIGHEST); //Set the priority of the car listing crawl to highest

            // Add to the array list
            jobListing.add(carObj);
        }

        // Crawl another page if there's a next page
        if (finalResult.getNextPage() != null) {
            final String nextPageURL = finalResult.getNextPage();

            // Schedule the next page
            scheduler.add(new VRequest(nextPageURL), this);
        }

    }

}
