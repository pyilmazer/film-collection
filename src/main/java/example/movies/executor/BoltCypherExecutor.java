package example.movies.executor;

import example.movies.util.Constants;
import org.neo4j.driver.v1.*;
import scala.collection.immutable.Stream;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by pinar on 27.05.2016.
 */
public class BoltCypherExecutor implements CypherExecutor {

    private final Driver driver;

    public BoltCypherExecutor(String url) {
        this(url, Constants.USERNAME, Constants.PASSWORD);
    }

    public BoltCypherExecutor(String url, String username, String password) {
        driver =  GraphDatabase.driver(Constants.DRIVER_URL, AuthTokens.basic( username, password ) );

    }

    @Override
    public Iterator<Map<String, Object>> query(String query, Map<String, Object> params) {
        try (Session session = driver.session()) {
            List<Map<String, Object>> list = session.run(query, params)
                    .list( r -> r.asMap(BoltCypherExecutor::convert));
            return list.iterator();
        }
    }

    @Override
    public Iterator<Map<String, Object>> query(String query) {
        try (Session session = driver.session()) {
            List<Map<String, Object>> list = session.run(query)
                    .list( r -> r.asMap(BoltCypherExecutor::convert));
            return list.iterator();
        }
    }
    @Override
    public void query2(String query){
        try (Session session = driver.session()) {
            session.run(query);

        }
    }

    static Object convert(Value value) {
        switch (value.type().name()) {
            case "PATH":
                return value.asList(BoltCypherExecutor::convert);
            case "NODE":
            case "RELATIONSHIP":
                return value.asMap();
        }
        return value.asObject();
    }

}
