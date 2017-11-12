package controller;

import javax.ws.rs.GET;  //REST-related dependencies
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import crypt.api.hashs.Hasher; //module to test dependencies
import crypt.factories.HasherFactory;
import rest.api.ServletPath;
import rest.factories.RestServerFactory;
import rest.impl.JettyRestServer;
import rest.api.RestServer;

@ServletPath("/command/test/*")  //url path. PREFIX WITH COMMAND/ !!!
@Path("/")
public class ResilientCommander {
    @GET
    @Path("/{input}") //a way to name the pieces of the query
    public String test(@PathParam("input") String input) { //this argument will be initialized with the piece of the query
        return new String("test : " + input + "----------------------------------------------------");
    }

    public static void main(String args[]) throws Exception {
    	RestServerFactory.createAndStartRestServer("jetty", 8080, "controller");
    }
}
