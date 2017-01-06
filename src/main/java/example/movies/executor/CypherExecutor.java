package example.movies.executor;

import java.util.Iterator;
import java.util.Map;


/**
 * Created by pinar on 27.05.2016.
 */
public interface CypherExecutor {
    Iterator<Map<String,Object>> query(String statement, Map<String, Object> params);
    Iterator<Map<String,Object>> query(String statement);
    void query2(String query);
}
