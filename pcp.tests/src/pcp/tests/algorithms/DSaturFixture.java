package pcp.tests.algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.AlgorithmException;

import pcp.algorithms.Preprocessor;
import pcp.algorithms.bounding.TimeBounder;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.ColoringVerifier;
import pcp.algorithms.coloring.DSaturColoring;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.model.parsing.DimacsParseException;
import pcp.model.parsing.DimacsParser;
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
	
	@Test
	public void shouldColorSmallGraph() throws AlgorithmException, DimacsParseException, IOException {
		DimacsParser parser = new DimacsParser();
		StringReader sreader = new StringReader("c nodecnt=10\nc density=0.6\np rand100 10 28 4\nq 1 2 3 4\nq 5 6 7\nq 8 9\nq 10\ne 1 2\ne 1 4\ne 1 5\ne 1 7\ne 1 8\ne 2 3\ne 2 4\ne 2 8\ne 2 9\ne 2 10\ne 3 4\ne 3 5\ne 3 8\ne 3 9\ne 4 6\ne 4 8\ne 4 9\ne 5 6\ne 5 7\ne 5 9\ne 5 10\ne 6 7\ne 6 8\ne 6 10\ne 7 8\ne 7 10\ne 8 9\ne 9 10\n");
		BufferedReader reader = new BufferedReader(sreader);
		this.builder = parser.parse(reader);
		
		check(2,5, true);
	}
	
	
	protected void check(int min, int max) throws AlgorithmException {
		check(min, max, false);
	}
	
	protected void check(int min, int max, boolean preprocess) throws AlgorithmException {
		graph = preprocess ? new Preprocessor(builder).preprocess().getGraph() : builder.getGraph();
		
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
