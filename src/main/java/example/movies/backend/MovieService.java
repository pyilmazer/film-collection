package example.movies.backend;

import example.movies.executor.BoltCypherExecutor;
import example.movies.executor.CypherExecutor;
import org.neo4j.helpers.collection.Iterators;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.neo4j.helpers.collection.MapUtil.map;


/**
 * Created by pinar on 27.05.2016.
 */

public class MovieService {

    private final CypherExecutor cypher;

    public MovieService(String uri) {
        cypher = createCypherExecutor(uri);
    }

    private CypherExecutor createCypherExecutor(String uri) {
        try {
            String auth = new URL(uri.replace("bolt","http")).getUserInfo();
            if (auth != null) {
                String[] parts = auth.split(":");
                return new BoltCypherExecutor(uri,parts[0],parts[1]);
            }
            return new BoltCypherExecutor(uri);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Neo4j-ServerURL " + uri);
        }
    }

    public void addActor(HashMap<String, Integer> actorList){

        Iterator<String> keySetIterator = actorList.keySet().iterator();
        while(keySetIterator.hasNext()){
            String key = keySetIterator.next();
            System.out.println("key: " + key + " value: " + actorList.get(key));
            key.replaceAll("\'","");
            Map<String, Object> row = new HashMap<>();
            row.put("title",key);
            row.put("id",actorList.get(key));
            cypher.query("CREATE (hebele" + actorList.get(key) + ":Actor {name:{title},id:{id}})", row);

        }

    }

    public void addMovie(ArrayList<Movies> movieList){

        for (int i = 0; i < movieList.size() ; i++) {

            Map<String, Object> row = new HashMap<>();
            row.put("title",movieList.get(i).getTitle());
            row.put("id",movieList.get(i).getId());
            row.put("released",movieList.get(i).getReleasedYear());
            row.put("rating",movieList.get(i).getRating());
            row.put("genre",movieList.get(i).getGenre());

            cypher.query("CREATE (m:MovieS {title:{title},id:{id},released:{released},rating:{rating},genre:{genre}})",row);


            ArrayList<Actors> aList = movieList.get(i).getActorList();
            for(int j=0;j<aList.size();j++) {

                Map<String, Object> row2 = new HashMap<>();
                row2.put("actorId", aList.get(j).getUserId());
                row2.put("movieId", movieList.get(i).getId());


                cypher.query("MATCH (a:Actor),(m:MovieS) " +
                        "WHERE a.id = {actorId} AND m.id = {movieId} " +
                        "CREATE (a)-[r:ACT_IN{hebele:1}]->(m) ",row2);

            }

        }
    }


    public void addCollectRelation(String collectorId,String movieId){

        Map<String, Object> row = new HashMap<>();
        row.put("collectorId", collectorId);
        row.put("movieId", movieId);


        cypher.query("MATCH (c:Collector),(m:MovieS) " +
                "WHERE c.id = {collectorId} AND m.id = {movieId} " +
                "CREATE (c)-[r:COLLECTS]->(m) ",row);

    }


    public void addFollowRelation(String collectorId1,String collectorId2){

        Map<String, Object> row = new HashMap<>();
        row.put("collectorId1", collectorId1);
        row.put("collectorId2", collectorId2);


        cypher.query("MATCH (c1:Collector),(c2:Collector) " +
                "WHERE c1.id = {collectorId1} AND c2.id = {collectorId2} " +
                "CREATE (c1)-[r:FOLLOWS]->(c2) ",row);

    }

    public void addDirector(HashMap<String, Integer> directorList){

        Iterator<String> keySetIterator = directorList.keySet().iterator();
        while(keySetIterator.hasNext()){
            String key = keySetIterator.next();
            System.out.println("key: " + key + " value: " + directorList.get(key));
            String[] splitted = key.split(" ");
            String nodeName =splitted[0] + splitted[1].substring(0,1);

            Map<String, Object> row = new HashMap<>();
            row.put("title", key);
            row.put("id",directorList.get(key));


            cypher.query("CREATE (d:Director {name:{title},id:{id}})", row);

        }

    }

    public void addCollector(ArrayList<Collectors> collectorList){

        for (int i = 0; i < collectorList.size(); i++) {
            Map<String, Object> row = new HashMap<>();

            row.put("fullName", collectorList.get(i).getFullName());
            row.put("userId", collectorList.get(i).getUserId());
            row.put("mail", collectorList.get(i).getMail());
            row.put("reference", collectorList.get(i).getReference());

            cypher.query("CREATE (c:Collector {name:{fullName},id:{userId}, mail:{mail}, reference:{reference}})", row);
        }

    }


    public Map getActor(String title){

        Map<String, Object> row =  map("name", title);


        Iterator<Map<String,Object>> result = cypher.query(
                "MATCH (hebele:Actor {name:{title}})" +
                        " RETURN hebele.name as name", map("title", title));

        while (result.hasNext()) {
            row = result.next();
            System.out.println(row.get("name"));
        }

        return row;

    }

    public Map findMovie(String title) {
        if (title==null) return Collections.emptyMap();
        return Iterators.singleOrNull(cypher.query(
                "MATCH (movie:Movie {title:{title}})" +
                " OPTIONAL MATCH (movie)<-[r]-(person:Person)\n" +
                " RETURN movie.title as title, collect({name:person.name, job:head(split(lower(type(r)),'_')), role:r.roles}) as cast LIMIT 1",
                map("title", title)));
    }


    @SuppressWarnings("unchecked")
    public ArrayList<Actors> route1() {
        Map<Object, Object> mapObj = new HashMap<>();
        ArrayList<Actors> list = new ArrayList<Actors>();
        Map<String, Object> row;
        System.out.println("Route1 MovieService");
        Iterator<Map<String,Object>> result = cypher.query(
                "MATCH (a:Actor),(d:Director)\n" +
                        " WHERE a.name = d.name\n" +
                        " RETURN a.name as name,a.id as id"
        );
        while (result.hasNext()) {
            row = result.next();
            Actors a = new Actors();
            String id = row.get("id").toString();
            a.setUserId(Integer.parseInt(id));
            a.setFullName((String) row.get("name"));
            list.add(a);
            mapObj.put(row.get("name"),row.get("id"));
            System.out.println(row.get("name") + " " + row.get("id"));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Actors> route2() {

        ArrayList<Actors> list = new ArrayList<>();
        Map<String, Object> row;
        System.out.println("Route1 MovieService");
        Iterator<Map<String,Object>> result = cypher.query(
                "MATCH (a:Actor)-[r:ACT_IN]->() WITH a,count(r) AS count\n"+
                        " WHERE count > 4 MATCH(a)-[r:ACT_IN]->() RETURN DISTINCT a.name as name, a.id as id");

        while (result.hasNext()) {
            row = result.next();
            Actors a = new Actors();
            String id = row.get("id").toString();
            a.setUserId(Integer.parseInt(id));
            a.setFullName((String) row.get("name"));
            list.add(a);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Actors> route3() {

        Map<String, Object> row = new HashMap<>();
        row.put("name", "Edward Norton");
        ArrayList<Actors> list = new ArrayList<>();
        System.out.println("Route3 MovieService");
        Iterator<Map<String,Object>> result = cypher.query(
                "MATCH (a:Actor)-[r:ACT_IN]->(m:MovieS) WHERE a.name = {name}\n"+
                        " WITH m as m MATCH(a:Actor)-[r:ACT_IN]->(m)\n"+
                        " WHERE a.name <> {name} RETURN  a.name as name,a.id as id",row);
        while (result.hasNext()) {
            row = result.next();
            Actors a = new Actors();
            String id = row.get("id").toString();
            a.setUserId(Integer.parseInt(id));
            a.setFullName((String) row.get("name"));
            list.add(a);
        }
        return list;
    }


    @SuppressWarnings("unchecked")
    public ArrayList<Collectors> route4() {

        Map<String, Object> row = new HashMap<>();
        row.put("name", "Edward Norton");
        ArrayList<Collectors> list = new ArrayList<>();
        System.out.println("Route4 MovieService");
        Iterator<Map<String,Object>> result = cypher.query(
                "MATCH(a:Actor)-[r:ACT_IN]->(m:MovieS) WHERE a.name = {name} WITH count(r) as t\n" +
                " MATCH (c:Collector)-[r:COLLECTS]->(m:MovieS) WITH m as m, c as c, t as t\n"+
                        "MATCH (a:Actor)-[r:ACT_IN]->(m) WHERE a.name = {name} WITH count(r) as counter, c as c, t as t\n"+
                        "WHERE t = counter RETURN c.name as fullname, c.id as userid",row);
        while (result.hasNext()) {
            row = result.next();
            Collectors a = new Collectors();
            String id = row.get("userid").toString();
            a.setUserId(id);
            a.setFullName((String) row.get("fullname"));
            list.add(a);
        }
        return list;
    }



    @SuppressWarnings("unchecked")
    public ArrayList<Collectors> route5() {

        Map<String, Object> row = new HashMap<>();
        row.put("title", "The Shawshank Redemption");
        ArrayList<Collectors> list = new ArrayList<>();
        System.out.println("Route5 MovieService");
        Iterator<Map<String,Object>> result = cypher.query(
                "MATCH (c:Collector)-[r:COLLECTS]->(m:MovieS) WHERE m.title={title}\n"+
                        " RETURN c.name as fullname, c.id as userId LIMIT 10",row);

        while (result.hasNext()) {
            row = result.next();
            Collectors a = new Collectors();
            String id = row.get("userId").toString();
            a.setUserId(id);
            a.setFullName((String) row.get("fullname"));
            list.add(a);
        }
        return list;
    }


    @SuppressWarnings("unchecked")
    public ArrayList<Collectors> route6() {

        Map<String, Object> row = new HashMap<>();
        row.put("userid", "1001");
        ArrayList<Collectors> list = new ArrayList<>();
        System.out.println("Route5 MovieService");

        Iterator<Map<String,Object>> result = cypher.query("MATCH (c1:Collector)-[f:FOLLOWS]->(c2:Collector) WHERE c1.id={userid} RETURN c2.name as name,c2.id as id UNION\n"+
                " MATCH (c1:Collector)-[f:FOLLOWS]->(c2:Collector)-[f2:FOLLOWS]->(c3:Collector) WHERE c1.id={userid} AND NOT ((c1)-[:FOLLOWS]->(c3)) RETURN c3.name as name,c3.id as id UNION\n"+
                " MATCH (c1:Collector)-[f:FOLLOWS]->(c2:Collector)-[f2:FOLLOWS]->(c3:Collector)-[f3:FOLLOWS]->(c4:Collector) WHERE c1.id={userid}\n"+
                " AND NOT((c1)-[:FOLLOWS]->(c4)) AND NOT ((c2)-[f:FOLLOWS]->(c4)) RETURN c4.name as name,c4.id as id",row);

        while (result.hasNext()) {
            row = result.next();
            Collectors a = new Collectors();
            String id = row.get("id").toString();
            a.setUserId(id);
            a.setFullName((String) row.get("name"));
            list.add(a);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public Iterable<Map<String,Object>> search(String query) {
        if (query==null || query.trim().isEmpty()) return Collections.emptyList();
        return Iterators.asCollection(cypher.query(
                "MATCH (movie:Movie)\n" +
                " WHERE lower(movie.title) CONTAINS {part}\n" +
                " RETURN movie",
                map("part", query.toLowerCase())));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> graph(int limit) {
        Iterator<Map<String,Object>> result = cypher.query(
                "MATCH (m:MovieS)<-[:ACT_IN]-(a:Actor) " +
                " RETURN m.title as movie, collect(a.name) as cast " +
                " LIMIT {limit}", map("limit",limit));
        List nodes = new ArrayList();
        List rels= new ArrayList();
        int i=0;
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            nodes.add(map("title",row.get("movie"),"label","movie"));
            int target=i;
            i++;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> actor = map("title", name,"label","actor");
                int source = nodes.indexOf(actor);
                if (source == -1) {
                    nodes.add(actor);
                    source = i++;
                }
                rels.add(map("source",source,"target",target));
            }
        }
        return map("nodes", nodes, "links", rels);
    }
}
