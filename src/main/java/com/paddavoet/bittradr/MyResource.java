package com.paddavoet.bittradr;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.paddavoet.bittradr.heroku.Main;
import com.paddavoet.bittradr.integration.responses.bitfinex.QueryMarketResponse;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
    	Main.initialise();
    	QueryMarketResponse response = null;     		
     	try {
     	  	response = Main.BIT_FIN_EX_API.queryMarket();
     	}
     	catch (Exception e) {
     		
     		return "Somefink went wonk!!" + e.getMessage();
     	}
    	
     	
     	return "Hello, Heroku! Last Price: " + response.getLastPrice();
     	
    }
}
