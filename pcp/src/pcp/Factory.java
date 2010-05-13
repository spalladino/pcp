package pcp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.ConfigurationColoring;
import pcp.algorithms.coloring.DSaturPartitionColoringEasiestNodes;
import pcp.algorithms.coloring.DSaturPartitionColoringHardestPartition;
import pcp.algorithms.coloring.NodesColoring;
import pcp.algorithms.coloring.PartitionsColoring;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.interfaces.IFactory;
import pcp.model.parsing.DimacsParser;
import pcp.solver.CplexBranchAndBoundSolver;
import pcp.solver.CplexBranchAndCutSolver;
import pcp.solver.CplexDynamicSearchSolver;
import pcp.solver.PcpBranchAndCutSolver;
import pcp.solver.Solver;


public class Factory implements IFactory {
	
	private static Factory factory = new Factory();
	
	public static Factory get() {
		return factory;
	}

	public ColoringAlgorithm coloring(pcp.model.strategy.Coloring coloring, IPartitionedGraph graph) {
		switch (coloring) {
			case Configuration:
				return new ConfigurationColoring(graph);
			case Nodes:
				return new NodesColoring(graph);
			case Partitions:
				return new PartitionsColoring(graph);
			case DSaturEasyNode:
				return new DSaturPartitionColoringEasiestNodes(graph);
			case DSaturHardPartition:
				return new DSaturPartitionColoringHardestPartition(graph);
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
	
	public PartitionedGraphBuilder[] getGraphBuilders(String filename, int max) throws Exception {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filename));
			DimacsParser parser = new DimacsParser();
			List<PartitionedGraphBuilder> builders = new ArrayList<PartitionedGraphBuilder>();
			
			PartitionedGraphBuilder builder = null;
			while (null != (builder = parser.parse(in)) && max --> 0) {
				builders.add(builder);
			} return (PartitionedGraphBuilder[]) builders.toArray(new PartitionedGraphBuilder[builders.size()]);
		} catch (Exception e) {
			System.err.println("Error reading graphs from " + filename);
			System.err.println(e.getMessage());
			throw e;
		} finally {
			if (in != null) { 
				in.close();
			}
		}
	}
	
	public Solver createSolver(pcp.solver.Kind kind) throws Exception {
		try {
			switch (kind) {
				case CplexBranchAndBound:
					return new CplexBranchAndBoundSolver();
				case PcpCutAndBranch:
					return new PcpBranchAndCutSolver(true);
				case PcpBranchAndCut:
					return new PcpBranchAndCutSolver(false);
				case CplexBranchAndCutSearch:
					return new CplexBranchAndCutSolver();
				case CplexDynamicSearch:
					return new CplexDynamicSearchSolver();
				default:
					throw new Exception("Invalid solver kind: " + kind);
			}
		} catch (Exception e) {
			System.err.println("Error initializing solver");
			System.err.println(e.getMessage());
			throw e;
		}
	}
}
