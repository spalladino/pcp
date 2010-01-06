package pcp;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.IntParam;
import pcp.algorithms.Preprocessor;
import pcp.algorithms.Verifier;
import pcp.entities.PartitionedGraph;
import pcp.entities.PartitionedGraphBuilder;
import pcp.interfaces.IFactory;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.ModelBuilder;
import pcp.solver.Printer;
import pcp.solver.Solver;
public class Main {

	static String solutionPath = "../sol/";
	
	public static void main(String[] args) throws Exception {
		solve(args[0], args[1]);
		System.exit(0);
	}
	
	private static void solve(String filename, String runId) throws Exception {
		IFactory factory = Factory.get();
		BuilderStrategy strategy = BuilderStrategy.createDefault();

		PartitionedGraph graph;
		Model model;
		
		PartitionedGraphBuilder builder = factory.getGraphBuilder(filename);
		Solver solver = factory.createSolver();
		
		try {
			graph = new Preprocessor(builder).preprocess().getGraph();
			model = new ModelBuilder(graph, solver.getCplex()).buildModel(strategy); 
			solver.setModel(model);
		} catch (Exception e) {
			System.err.println("Error creating model");
			System.err.println(e.getMessage());
			return;
		}
		
		exportModel(solver, filename);
		
		System.out.println("Solving " + filename + " with " + graph.getNodes().length + " nodes and " + graph.getEdges().length + " edges.");
		solver.getCplex().setParam(IntParam.MIPDisplay, 3);
		solver.solve();
		
		if (solver.isSolved()) {
			new Printer(solver).printSolution(true);
			new Verifier(solver).verify();
			solver.getCplex().writeSolution(solutionPath + "run" + runId + ".sol");
			System.out.println("Chromatic number is " + solver.getChi());
			System.out.println("Solved in " + solver.getTime() + " ticks");
		} else {
			System.out.println("Solution failed");
		}
		
		solver.end();
	}

	private static void exportModel(Solver solver, String filename) throws IloException {
		solver.getCplex().exportModel(filename + ".lps");
	}

}
