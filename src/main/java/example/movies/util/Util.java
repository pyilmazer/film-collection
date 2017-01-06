package example.movies.util;

public class Util {

    public static int getWebPort() {
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            return Constants.PORT;
        }
        return Integer.parseInt(webPort);
    }

    public static String getNeo4jUrl() {
        String urlVar = System.getenv("NEO4J_URL_VAR");
        if (urlVar==null)
            urlVar = "NEO4J_URL";
        String url =  System.getenv(urlVar);
        if(url == null || url.isEmpty()) {
            return Constants.DRIVER_URL +":" + Constants.DRIVER_PORT;
        }
        return url;
    }
}
