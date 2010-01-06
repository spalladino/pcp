package pcp;

import java.io.BufferedReader;
import java.io.FileReader;

import pcp.algorithms.coloring.Coloring;
import pcp.algorithms.coloring.ConfigurationColoring;
import pcp.algorithms.coloring.NodesColoring;
import pcp.algorithms.coloring.PartitionsColoring;
import pcp.entities.PartitionedGraph;
import pcp.entities.PartitionedGraphBuilder;
import pcp.interfaces.IFactory;
import pcp.parsing.DimacsParser;
import pcp.solver.Solver;


public class Factory implements IFactory {
	
	private static Factory factory = new Factory();
	
	public static Factory get() {
		return factory;
	}

	public Coloring coloring(pcp.model.strategy.Coloring coloring, PartitionedGraph graph) {
		switch (coloring) {
			case Configuration:
				return new ConfigurationColoring(graph);
			case Nodes:
				return new NodesColoring(graph);
			case Partitions:
				return new PartitionsColoring(graph);
		}
		return null;
	}
	
	public PartitionedGraphBuilder getGraphBuilder(String filename) throws Exception {
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			DimacsParser parser = new DimacsParser();
			return parser.parse(in);
		} catch (Exception e) {
			System.err.println("Error reading graph from " + filename);
			System.err.println(e.getMessage());
			throw e;
		}
	}
	
	public Solver createSolver() throws Exception {
		try {
			return new Solver();
		} catch (Exception e) {
			System.err.println("Error initializing solver");
			System.err.println(e.getMessage());
			throw e;
		}
	}
}
