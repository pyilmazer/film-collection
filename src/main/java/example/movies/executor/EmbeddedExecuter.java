package example.movies.executor;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/**
 * Created by pinar on 03.06.2016.
 */
public class EmbeddedExecuter {


        private GraphDatabaseService db = null;

        public EmbeddedExecuter(){
            GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
            File file = new File("/home/pinar/Desktop/pinarNeo4jDB");
            GraphDatabaseService db= dbFactory.newEmbeddedDatabase(file);

           /* try (Transaction tx = db.beginTx()) {
                // Perform DB operations
                tx.success();
            }*/
        }

}
