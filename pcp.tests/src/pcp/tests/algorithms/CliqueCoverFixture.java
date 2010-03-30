package pcp.tests.algorithms;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pcp.algorithms.clique.CliqueCover;
import pcp.entities.ISimpleGraph;
import pcp.entities.simple.SimpleGraphBuilder;
import pcp.entities.simple.Node;


public class CliqueCoverFixture {
	
	SimpleGraphBuilder builder;
	ISimpleGraph graph;
	
	@Before
	public void setup() {
		builder = new SimpleGraphBuilder("test");
	}
	
	@Test
	public void test() {
		graph = builder.addNodes(4)
			.addEdge(0, 1)
			.addEdge(1, 2)
			.addEdge(2, 3)
			.addEdge(3, 0)
			.addEdge(3, 1)
			.getGraph();
		
		CliqueCover cover = new CliqueCover(graph);
		List<List<Node>> cliques = cover.cliques();
		
		Collections.sort(cliques, new Comparator<List<Node>>() {
			public int compare(List<Node> o1, List<Node> o2) {
				return o2.size() - o1.size();
			}
		});
		
		Assert.assertEquals(2, cliques.size());
		
		List<Node> clique = cliques.get(0);
		Collections.sort(clique);
		
		Assert.assertEquals(3, clique.size());
		Assert.assertEquals(0, clique.get(0).index());
		Assert.assertEquals(1, clique.get(1).index());
		Assert.assertEquals(3, clique.get(2).index());
		
		clique = cliques.get(1);
		Collections.sort(clique);
		Assert.assertEquals(1, clique.size());
		Assert.assertEquals(2, clique.get(0).index());
	}
	
}
