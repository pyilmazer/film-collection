package example.movies.backend;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class DataReader {

    private static Driver driver;
    private static MovieService mov_service;

    public DataReader(MovieService service) {
        this.mov_service = service;
    }

    public static void readMovies(){
        HashMap<String, Integer> actorMap = new HashMap<String, Integer>();
        HashMap<String, Integer> directorMap = new HashMap<String, Integer>();
        ArrayList<Movies> movieList = new ArrayList<Movies>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("FILMS.txt"));
            String line = br.readLine();
            while (line != null) {
                String [] splitted = line.split("%");
                Movies movie = new Movies();
                movie.setId(splitted[0].trim());
                movie.setTitle(splitted[1].trim());
                movie.setReleasedYear(splitted[2].trim());
                String [] splitted2 = splitted[3].split(",");
                for (int i = 0; i < splitted2.length; i++) {
                    String fullName = splitted2[i].trim();
                    if(actorMap.get(fullName) == null){ //no actor available --> add it
                        int maxValueInMap = 0;
                        if(!actorMap.isEmpty()){
                            maxValueInMap=(Collections.max(actorMap.values()));
                        }
                        int id = maxValueInMap+1;
                        actorMap.put(fullName,id);
                        movie.getActorList().add(addActor(fullName,id));
                    }else{
                        movie.getActorList().add(addActor(fullName,actorMap.get(fullName)));
                    }
                }

                movie.setGenre(splitted[4].trim());
                String directorName = splitted[5].trim();
                movie.setDirector(directorName);
                if(directorMap.get(directorName) == null) { //no actor available --> add it
                    int maxValueInMap = 0;
                    if (!directorMap.isEmpty()) {
                        maxValueInMap = (Collections.max(directorMap.values()));
                    }
                    int directorId = maxValueInMap + 1;
                    directorMap.put(directorName, directorId);
                }

                movie.setRating(splitted[6].trim());
                movieList.add(movie);
                line = br.readLine();
            }

            //printMovies(movieList);
            //printActors(actorMap);
            //printActors(directorMap);

            mov_service.addDirector(directorMap);
            mov_service.addActor(actorMap);
            mov_service.addMovie(movieList);

            br.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public static Actors addActor(String fullName, int id){
        Actors actor = new Actors();
        actor.setFullName(fullName);
        actor.setUserId(id);
        return actor;
    }


    public static void readCollect(){

        try {
            BufferedReader br = new BufferedReader(new FileReader("collect.txt"));
            String line = br.readLine();
            while (line != null) {
                String [] splitted = line.split("%");
                mov_service.addCollectRelation(splitted[0], splitted[1]);
                line = br.readLine();
            }
            br.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public static void readFollow(){

        try {
            BufferedReader br = new BufferedReader(new FileReader("follow.txt"));
            String line = br.readLine();
            while (line != null) {
                String [] splitted = line.split("%");
                mov_service.addFollowRelation(splitted[0], splitted[1]);
                line = br.readLine();
            }
            br.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }


    public static void readCollectors(){
        ArrayList<Collectors> collectorList = new ArrayList<Collectors>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("collectors.txt"));
            String line = br.readLine();
            while (line != null) {
                String [] splitted = line.split("%");
                Collectors collector = new Collectors();
                collector.setUserId(splitted[0].trim());
                collector.setFullName(splitted[1].trim());
                collector.setMail(splitted[2].trim());
                collector.setReference(splitted[3].trim());

                collectorList.add(collector);

                line = br.readLine();
            }

            mov_service.addCollector(collectorList);

            //printCollectors(collectorList);

            br.close();
        }catch (Exception e){
            System.out.println(e);
        }


    }

    static void printMovies(ArrayList<Movies> list){

        for (int i = 0; i < list.size()  ; i++) {
            System.out.println(list.get(i).getId() + " " + list.get(i).getTitle() + " " + list.get(i).getReleasedYear() + " " + list.get(i).getGenre() + " "+ list.get(i).getDirector() +" " + list.get(i).getRating());
            for (int j = 0; j <list.get(i).getActorList().size() ; j++) {
                System.out.println("    "+list.get(i).getActorList().get(j).getFullName());
            }
        }

    }


    static void printActors(HashMap<String, Integer> map){

        Iterator<String> keySetIterator = map.keySet().iterator();
        while(keySetIterator.hasNext()){
            String key = keySetIterator.next();
            System.out.println("key: " + key + " value: " + map.get(key));
        }

    }



    static void printCollectors(ArrayList<Collectors> list){

        for (int i = 0; i < list.size()  ; i++) {
            System.out.println(list.get(i).getUserId() + " " + list.get(i).getFullName() + " " + list.get(i).getMail() + " " + list.get(i).getReference());
        }

    }
}
