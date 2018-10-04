/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.preferred.crawler.example.master;

import ai.preferred.venom.request.Request;
import ai.preferred.venom.response.Response;
import ai.preferred.venom.response.VResponse;
import ai.preferred.venom.validator.Validator;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ween Jiann Lee
 */
public class ListingValidator implements Validator {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListingValidator.class);

  /**
   * Use this to positively validate your page.
   * <p>
   * For example, if you are crawling store ABC, you would find.
   * </p>
   *
   * @param request  The request used to fetch.
   * @param response The response fetched using request.
   * @return status of the validation
   */
  @Override
  public Status isValid(Request request, Response response) {
    final VResponse vResponse = new VResponse(response);

    // Do some checks here
    
    //TEST Commenting - Original
//    if (vResponse.getHtml().contains("Jobs in Singapore")) {
//      return Status.VALID;
//    }
    final Document document = vResponse.getJsoup();
    
    if (request.getUrl().startsWith("https://www.sgcarmart.com/used_cars/info.php?")){
        if (document.select("#main_left > div.blue_curvebanner.box_header > div").first() != null){
            return Status.VALID;
        }
        
    }
    else if (document.select("table > form[name=\"listingform\"]").first()!=null) {
        
      return Status.VALID;
    }

    return Status.INVALID_CONTENT;
  }

}
