package pcp.tests.parsing;

import static junit.framework.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.model.parsing.DimacsParseException;
import pcp.model.parsing.DimacsParser;


public class GraphParseFixture {
	
	BufferedReader reader;
	DimacsParser parser;
	IPartitionedGraph graph;
	
	@Before
	public void setUp() throws Exception {
		parser = new DimacsParser();
	}
	
	@After
	public void tearDown() throws Exception {
		try {
			if (reader != null) {
				reader.close();
			}
		} finally {
			reader = null;
			graph = null;
			parser = null;
		}
	}
	
	@Test(expected= DimacsParseException.class)
	public void testFailNodeCountNotMatches() throws Exception {
		parse(
				"p pcpg 2 2 2 \n" +
				"q 0 1\n" +
				"q 2  \n" +
				"e 0 1\n" +
				"e 0 2\n");		
	}
	
	@Test(expected= DimacsParseException.class)
	public void testFailEdgeCountNotMatches() throws Exception {
		parse(
				"p pcpg 3 3 2 \n" +
				"q 0 1\n" +
				"q 2  \n" +
				"e 0 1\n" +
				"e 0 2\n");		
	}
	
	@Test(expected= DimacsParseException.class)
	public void testFailPartitionCountNotMatches() throws Exception {
		parse(
				"p pcpg 3 2 1 \n" +
				"q 0 1\n" +
				"q 2  \n" +
				"e 0 1\n" +
				"e 0 2\n");		
	}
	
	@Test(expected= DimacsParseException.class)
	public void testInexistantNodeInPartition() throws Exception {
		parse(
				"p pcpg 3 2 2 \n" +
				"q 0 1\n" +
				"q 2 3\n" +
				"e 0 1\n" +
				"e 0 2\n");		
	}
	
	@Test(expected= DimacsParseException.class)
	public void testInexistantNodeInEdge() throws Exception {
		parse(
				"p pcpg 3 2 2 \n" +
				"q 0 1\n" +
				"q 2  \n" +
				"e 0 1\n" +
				"e 0 3\n");		
	}

	
	@Test
	public void testSimpleGraph() throws Exception {
		parse(
				"p pcpg 3 2 2 \n" +
				"q 0 1\n" +
				"q 2  \n" +
				"e 0 1\n" +
				"e 0 2\n");		
	
		assertGraphProperties();
	}
	
	@Test
	public void testBaseOneGraph() throws Exception {
		parser.setBase(1);
		parse(
				"p pcpg 3 2 2 \n" +
				"q 1 2\n" +
				"q 3  \n" +
				"e 1 2\n" +
				"e 1 3\n");		
	
		assertGraphProperties();
	}
	
	@Test
	public void testCommentedGraph() throws Exception {
		parser.setBase(1);
		parse(
				"c this is a comment\n" +
				"p pcpg 3 2 2 \n" +
				"c this is a comment\n" +
				"q 1 2\n" +
				"c this is a comment\n" +
				"q 3  \n" +
				"c this is a comment\n" +
				"e 1 2\n" +
				"c this is a comment\n" +
				"e 1 3\n");		
	
		assertGraphProperties();
	}

	@Test
	public void testMultipleGraphs() throws Exception {
		createReader(
				"p pcpg 3 2 2 \n" +
				"q 0 1\n" +
				"q 2  \n" +
				"e 0 1\n" +
				"e 0 2\n" +
				"p pcpg2 3 2 2 \n" +
				"q 0 1\n" +
				"q 2  \n" +
				"e 0 1\n" +
				"e 0 2\n");		
	
		nextGraph();
		assertGraphProperties("pcpg");
		
		nextGraph();
		assertGraphProperties("pcpg2");
	}

	private void assertGraphProperties() {
		assertGraphProperties("pcpg");
	}
	
	private void assertGraphProperties(String name) {
		assertProperties(name);
		if (graph instanceof PartitionedGraphBuilder) {
			PartitionedGraphBuilder builder = (PartitionedGraphBuilder) graph;
			graph = builder.getGraph();
			assertProperties(name);
		}
	}
	
	private void assertProperties(String name) {
		assertEquals(3, graph.getNodes().length);
		assertEquals(2, graph.getEdges().length);
		assertEquals(2, graph.getPartitions().length);
			
		assertEquals(name, graph.getName());
		
		Node n0 = graph.getNodes()[0];
		Node n1 = graph.getNodes()[1];
		Node n2 = graph.getNodes()[2];
		
		Partition p0 = graph.getPartitions()[0];
		Partition p1 = graph.getPartitions()[1];
		
		Edge e0 = graph.getEdges()[0];
		Edge e1 = graph.getEdges()[1];
		
		assertEquals(0, n0.index());
		assertEquals(1, n1.index());
		assertEquals(2, n2.index());
		
		assertEquals(2, n0.getNeighbours().length);
		assertEquals(n1, n0.getNeighbours()[0]);
		assertEquals(n2, n0.getNeighbours()[1]);
		
		assertEquals(1, n1.getNeighbours().length);
		assertEquals(n0, n1.getNeighbours()[0]);
		
		assertEquals(1, n2.getNeighbours().length);
		assertEquals(n0, n2.getNeighbours()[0]);
		
		assertEquals(true, graph.areAdjacent(n0, n1));
		assertEquals(true, graph.areAdjacent(n0, n2));
		assertEquals(true, graph.areAdjacent(n1, n0));
		assertEquals(true, graph.areAdjacent(n2, n0));
		assertEquals(false, graph.areAdjacent(n1, n2));
		assertEquals(false, graph.areAdjacent(n2, n1));
		
		assertEquals(0, p0.index());
		assertEquals(1, p1.index());
	
		assertEquals(2, p0.getNodes().length);
		assertEquals(1, p1.getNodes().length);
		
		assertEquals(n0, p0.getNodes()[0]);
		assertEquals(n1, p0.getNodes()[1]);
		assertEquals(n2, p1.getNodes()[0]);
		
		assertEquals(p0, n0.getPartition());
		assertEquals(p0, n1.getPartition());
		assertEquals(p1, n2.getPartition());
		
		assertEquals(n0, e0.getNode1());
		assertEquals(n1, e0.getNode2());

		assertEquals(n0, e1.getNode1());
		assertEquals(n2, e1.getNode2());
	}

	private void parse(String str) throws Exception {
		createReader(str);
		nextGraph();
	}
	
	private void nextGraph() throws Exception {
		graph = parser.parse(reader);
	}
	
	private void createReader(String str) {
		StringReader sr = new StringReader(str);
		BufferedReader br = new BufferedReader(sr);
		this.reader = br;
	}
	
}
