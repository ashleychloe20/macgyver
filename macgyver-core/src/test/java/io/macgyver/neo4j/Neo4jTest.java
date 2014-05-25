package io.macgyver.neo4j;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.springframework.data.neo4j.support.GraphDatabaseServiceFactoryBean;

import com.google.common.io.Files;

public class Neo4jTest {

	@Test
	public void testIt() throws Exception {

		
		GraphDatabaseServiceFactoryBean b = new GraphDatabaseServiceFactoryBean(
				Files.createTempDir().getAbsolutePath());
		GraphDatabaseService svc = b.getObject();

		Assert.assertNotNull(svc);

		IndexDefinition indexDefinition;
		try ( Transaction tx = svc.beginTx() )
		{
		    Schema schema = svc.schema();
		    ConstraintDefinition def = schema.constraintFor(DynamicLabel.label("User")).assertPropertyIsUnique("name").create();
		
		/*    indexDefinition = schema.indexFor( DynamicLabel.label( "User" ) )
		            .on( "name" )
		            .create();*/
		    tx.success();
		}
		try ( Transaction tx = svc.beginTx() )
		{
		    Schema schema = svc.schema();
		 //   schema.awaitIndexOnline( indexDefinition, 10, TimeUnit.SECONDS );
		}
		
		Label l = DynamicLabel.label("User");
		try (Transaction tx = svc.beginTx()) {

			for (int i = 0; i < 100; i++) {
				Node n = svc.createNode(l);

				n.setProperty("name", "user" + i);

				n.setProperty("hello", "abc+"+i);
//				n = svc.createNode(l);

	//			n.setProperty("name", "user" + i);
			}

			tx.success();
		}

		try (Transaction tx = svc.beginTx()) {
			
			try (ResourceIterator<Node> users = svc
					.findNodesByLabelAndProperty(l, "hello", "abc3").iterator()) {
				ArrayList<Node> userNodes = new ArrayList<>();
				while (users.hasNext()) {
					userNodes.add(users.next());
				}

				for (Node node : userNodes) {
					System.out.println(node);

				}
			}
		}
		
		
		ExecutionEngine engine = new ExecutionEngine( svc );
String rows="";

		org.neo4j.cypher.javacompat.ExecutionResult result;
		try ( Transaction ignored = svc.beginTx() )
		{
		    result = engine.execute( "start n=node(*) where n.hello = 'abc3' return n, n.name" );
		    for ( Map<String, Object> row : result )
		    {
		        for ( Entry<String, Object> column : row.entrySet() )
		        {
		            rows += column.getKey() + ": " + column.getValue() + "; ";
		        }
		        rows += "\n";
		    }
		    System.out.println(rows);
		    ignored.success();
		}

	}
}
