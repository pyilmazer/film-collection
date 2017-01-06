package example.movies.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import java.net.URLDecoder;
import java.util.ArrayList;

import static spark.Spark.get;

public class MovieRoutes implements SparkApplication {

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private MovieService service;

    public MovieRoutes(MovieService service) {
        this.service = service;
    }

    public void init() {
        get(new Route("/movie/:title") {
            public Object handle(Request request, Response response) {
                return gson.toJson(service.getActor(URLDecoder.decode(request.params("title"))));
            }
        });
        get(new Route("/search") {
            public Object handle(Request request, Response response) {
                return gson.toJson(service.getActor(request.queryParams("q")));
            }
        });
        get(new Route("/graph") {
            public Object handle(Request request, Response response) {
                int limit = request.queryParams("limit") != null ? Integer.valueOf(request.queryParams("limit")) : 100;
                return gson.toJson(service.graph(limit));
            }
        });


        get(new Route("/route1") {
            public Object handle(Request request, Response response) {
                ArrayList<Actors> a = service.route1();
                return gson.toJson(a);
            }
        });


        get(new Route("/route2") {
            public Object handle(Request request, Response response) {
                ArrayList<Actors> a = service.route2();
                return gson.toJson(a);
            }
        });

        get(new Route("/route3") {
            public Object handle(Request request, Response response) {
                ArrayList<Actors> a = service.route3();
                return gson.toJson(a);
            }
        });

        get(new Route("/route4") {
            public Object handle(Request request, Response response) {
                ArrayList<Collectors> a = service.route4();
                return gson.toJson(a);
            }
        });

        get(new Route("/route5") {
            public Object handle(Request request, Response response) {
                ArrayList<Collectors> a = service.route5();
                return gson.toJson(a);
            }
        });

        get(new Route("/route6") {
            public Object handle(Request request, Response response) {
                ArrayList<Collectors> a = service.route6();
                return gson.toJson(a);
            }
        });
    }
}
