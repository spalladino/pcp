package pcp.tests.algorithms;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pcp.algorithms.clique.MaxCliqueFinder;
import pcp.entities.ISimpleGraph;
import pcp.entities.simple.Node;
import pcp.entities.simple.SimpleGraphBuilder;
import pcp.tests.GraphGeneration;
import pcp.utils.SimpleGraphUtils;
import props.Settings;
import exceptions.AlgorithmException;

public class MaxCliqueFinderFixture {

	SimpleGraphBuilder builder;
	ISimpleGraph graph;
	
	public MaxCliqueFinderFixture() throws Exception {
		Settings.load("test");
	}
	
	@Before
	public void setup() {
		builder = new SimpleGraphBuilder("test");
	}
	
	@Test
	public void testCompleteGraph() {
		builder.addNodes(5)
			.addEdges(0, 1,2,3,4)
			.addEdges(1, 2,3,4)
			.addEdges(2, 3,4)
			.addEdges(3, 4);
		
		test(5);
	}
	
	@Test
	public void testGraph01() {
		builder.addNodes(5)
			.addEdges(0, 1)
			.addEdges(1, 2,3,4)
			.addEdges(2, 3,4)
			.addEdges(3, 4);
		
		test(4);
	}
	
	@Test
	public void testGraph02() {
		builder.addNodes(6)
			.addEdges(0, 1)
			.addEdges(1, 2,3,4)
			.addEdges(2, 3,4)
			.addEdges(3, 4)
			.addEdges(5, 0,2,3,4);
		
		test(4);
	}
	
	@Test
	public void testGraph03() {
		builder.addNodes(6)
			.addEdges(0, 1,2,3)
			.addEdges(1, 2,3)
			.addEdges(2, 3)
			.addEdges(3, 4)
			.addEdges(4, 0,2,3,5);
		
		test(4);
	}
	
	
	@Test
	public void testRandomGraphs() throws AlgorithmException, IOException {
		int count = 100;
		while (count --> 0) {
			graph = GraphGeneration.createGraph(40, 2, 4, 0.5).getGPrime();
			check();
		}
	}
	
	
	private void test(int expected) {
		graph = builder.getGraph();
		check(expected);
	}

	private void check() {
		MaxCliqueFinder finder = new MaxCliqueFinder(graph, null).run();
		List<Node> clique = finder.getClique();
		if (clique != null) {
			Assert.assertTrue(SimpleGraphUtils.checkClique(graph, clique));
		}
	}
	
	private void check(int expected) {
		MaxCliqueFinder finder = new MaxCliqueFinder(graph, null).run();
		List<Node> clique = finder.getClique();
		Assert.assertTrue(SimpleGraphUtils.checkClique(graph, clique));
		Assert.assertEquals(expected, clique.size());
	}
	
}
