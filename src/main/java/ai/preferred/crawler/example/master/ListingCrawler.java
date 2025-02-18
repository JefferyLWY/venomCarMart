/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.preferred.crawler.example.master;

import ai.preferred.crawler.example.EntityCSVStorage;
import ai.preferred.crawler.example.entity.Car;
import ai.preferred.crawler.example.entity.Listing;
import ai.preferred.venom.Crawler;
import ai.preferred.venom.Session;
import ai.preferred.venom.fetcher.AsyncFetcher;
import ai.preferred.venom.fetcher.Fetcher;
import ai.preferred.venom.request.VRequest;
import ai.preferred.venom.validator.EmptyContentValidator;
import ai.preferred.venom.validator.PipelineValidator;
import ai.preferred.venom.validator.StatusOkValidator;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author Ween Jiann Lee
 */
public class ListingCrawler {

    // You can use this to log to console
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ListingCrawler.class);

    // Create session keys for things you would like to retrieve from handler
    static final Session.Key<ArrayList<Car>> JOB_LIST_KEY = new Session.Key<>();

    // Create session keys for CSV printer to print from handler
    static final Session.Key<EntityCSVStorage> CSV_STORAGE_KEY = new Session.Key<>();

    public static void main(String[] args) {

        // Get project directory
        String workingDir = System.getProperty("user.dir");
        System.out.println(workingDir);
        // Start CSV printer
        try (final EntityCSVStorage printer = new EntityCSVStorage(workingDir + "/cars.csv")) {

            // Let's init the session, this allows us to retrieve the array list in the handler
            final ArrayList<Car> jobListing = new ArrayList<>();
            final Session session = Session.builder()
                    .put(JOB_LIST_KEY, jobListing)
                    .put(CSV_STORAGE_KEY, printer)
                    .build();

            // Start crawler
            try (final Crawler crawler = crawler(fetcher(), session).start()) {
                LOGGER.info("Starting crawler...");

                //Set URL for used cars page displaying only available cars and with 100 results per page
                final String startUrl = "https://www.sgcarmart.com/used_cars/listing.php?RPG=100&AVL=2&ORD=";

                // pass in URL and handler or use a HandlerRouter
                crawler.getScheduler().add(new VRequest(startUrl), new ListingHandler());
            }

            // We will retrieve all the cars here
            LOGGER.info("We have found {} listings!", jobListing.size());

        } catch (Exception e) {
            LOGGER.error("Could not run crawler: ", e);
        }

    }

    private static Fetcher fetcher() {
        // You can look in builder the different things you can add
        return AsyncFetcher.builder()
                .validator(new PipelineValidator(
                                EmptyContentValidator.INSTANCE,
                                StatusOkValidator.INSTANCE,
                                new ListingValidator()))
                .build();
    }

    private static Crawler crawler(Fetcher fetcher, Session session) {
        // You can look in builder the different things you can add
        return Crawler.builder()
                .fetcher(fetcher)
                .session(session)
                .build();
    }
}
