package example.movies.backend;

import example.movies.util.Constants;
import example.movies.util.Util;

import java.util.concurrent.TimeUnit;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.setPort;

/**
 * Created by pinar on 27.05.2016.
 */

public class MovieServer {

    public static void main(String[] args) {
        try {
            setPort(Util.getWebPort());
            externalStaticFileLocation(Constants.WEBAPP_LOCATION);
            final MovieService service = new MovieService(Util.getNeo4jUrl());
            new MovieRoutes(service).init();

            DataReader dr = new DataReader(service);
            dr.removeAllNodesRels();

            System.out.println("All Nodes & Relations are removed");
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Create New Nodes");
            //System.out.println("Read Movies");
            dr.readMovies();
            //System.out.println("Read Collectors");
            dr.readCollectors();
            //System.out.println("Read Collect");
            dr.readCollect();
            //System.out.println("Read Follow");
            dr.readFollow();
            //System.out.println("Reading is Finished");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
