package pcp.tests.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.AlgorithmException;

import pcp.algorithms.bounding.TimeBounder;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.ColoringVerifier;
import pcp.algorithms.coloring.DSaturColoring;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import props.Settings;

public class DSaturFixture {
	
	protected ColoringAlgorithm dsatur;
	protected PartitionedGraphBuilder builder;
	protected List<Node> clique;
	
	protected IPartitionedGraph graph;
	protected TimeBounder bounder;
	
	@BeforeClass
	public static void load() throws Exception {
		Settings.load("test");
	}
	
	@Before
	public void setup() {
		builder = new PartitionedGraphBuilder("test");
		clique = new ArrayList<Node>();
		bounder = new TimeBounder();
	}
	
	@Test
	public void shouldReturnValidColoring() throws AlgorithmException {
		builder.createNodes(6);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		builder.addEdge(1, 3);
		builder.addEdge(5, 0);
		builder.addEdge(5, 2);
		
		clique.add(builder.getNode(1));
		clique.add(builder.getNode(2));
		clique.add(builder.getNode(3));
		
		check(3,3);
	}
	
	@Test
	public void shouldReturnValidNonOptimalColoring() throws AlgorithmException {
		builder.createNodes(6);
		
		builder.addEdge(4, 0);
		builder.addEdge(0, 1);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(5, 2);
		builder.addEdge(1, 3);
		builder.addEdge(5, 0);
		builder.addEdge(1, 2);
		
		
		clique.add(builder.getNode(1));
		clique.add(builder.getNode(2));
		clique.add(builder.getNode(3));
		
		bounder.setMaxTime(0);
		check(3,6);
	}
	
	@Test
	public void shouldReturnValidColoringSingletonClique() throws AlgorithmException {
		builder.createNodes(6);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		builder.addEdge(1, 3);
		builder.addEdge(5, 0);
		builder.addEdge(5, 2);
		
		clique.add(builder.getNode(1));
		
		check(3,3);
	}
	
	@Test
	public void shouldReturnValidColoringShuffle() throws AlgorithmException {
		builder.createNodes(6);
		
		builder.addEdge(4, 0);
		builder.addEdge(0, 1);
		builder.addEdge(5, 2);
		builder.addEdge(3, 4);
		builder.addEdge(1, 3);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(5, 0);

		
		clique.add(builder.getNode(1));
		clique.add(builder.getNode(2));
		clique.add(builder.getNode(3));
		
		check(3,3);
	}
	
	@Test
	public void shouldReturnValidColoringOddHole() throws AlgorithmException {
		builder.createNodes(5);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		
		clique.add(builder.getNode(1));
		clique.add(builder.getNode(2));
		
		check(3,3);
	}
	
	@Test
	public void shouldReturnValidColoringEvenHole() throws AlgorithmException {
		builder.createNodes(6);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 5);
		builder.addEdge(5, 0);
		
		clique.add(builder.getNode(1));
		clique.add(builder.getNode(2));
		
		check(2,2);
	}
	
	
	protected void check(int min, int max) throws AlgorithmException {
		graph = builder.getGraph();
		
		dsatur = createDSatur();
		int c = dsatur.getChi();
		
		Assert.assertTrue("Chi " + c + " is not within expected bounds", c >= min);
		Assert.assertTrue("Chi " + c + " is not within expected bounds", c <= max);

		new ColoringVerifier(graph).verify(dsatur);
	}
	
	protected ColoringAlgorithm createDSatur() {
		 DSaturColoring saturColoring = new DSaturColoring(graph);
		 saturColoring.setClique(clique);
		 saturColoring.withBounder(bounder);
		 return saturColoring;
	}

	
}
