package pcp.solver.io;

import ilog.concert.IloException;

import java.io.PrintStream;

import pcp.entities.IPartitionedGraph;
import pcp.interfaces.IModelData;
import pcp.utils.ArrayUtils;

public class IterationPrinter {

	IPartitionedGraph graph;
	IModelData data;
	
	PrintStream out = System.out;
	
	public IterationPrinter(IModelData data, IPartitionedGraph graph) {
		this.data = data;
		this.graph = graph;
	}
	
	public void printVerboseIteration() throws IloException {
		out.println("Iteration values:");
		out.println("W: " + ArrayUtils.toString(data.ws()));
		for (int n = 0; n < graph.getNodes().length; n++) {
			out.println("X[" + n + "]: " + ArrayUtils.toString(data.xs(n)));
		}
		out.println();
	}
	
	public void printColorsIteration() throws IloException {
		out.println("W: " + ArrayUtils.toString(data.ws()));
	}
	
}
