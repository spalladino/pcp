package pcp;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.IntParam;
import pcp.algorithms.Preprocessor;
import pcp.algorithms.Verifier;
import pcp.algorithms.connectivity.ConnectivityChecker;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.interfaces.IFactory;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.ModelBuilder;
import pcp.solver.Kind;
import pcp.solver.Solver;
import pcp.solver.io.Printer;
public class Main {

	public static void main(String[] args) throws Exception {
		String filename, runId;
		Settings.load("run");
		
		if (args.length >= 2) {
			filename = args[0];
			runId = args[1];
		} else {
			filename = Settings.get().getString("run.filename");
			runId = Settings.get().getString("run.id");
		}
		
		solve(filename, runId);
		System.exit(0);
	}
	
	private static void solve(String filename, String runId) throws Exception {
		IFactory factory = Factory.get();
		BuilderStrategy strategy = BuilderStrategy.fromSettings();

		PartitionedGraph graph;
		Model model;
		
		PartitionedGraphBuilder builder = factory.getGraphBuilder(filename);
		Solver solver = factory.createSolver(Settings.get().getEnum("solver.kind", Kind.class));
		
		try {
			graph = new Preprocessor(builder).preprocess().getGraph();
			model = new ModelBuilder(graph, solver.getCplex()).buildModel(strategy); 
			solver.setModel(model);
		} catch (Exception e) {
			System.err.println("Error creating model");
			System.err.println(e.getMessage());
			return;
		}

		if (!new ConnectivityChecker(graph).check()) {
			System.err.println("Graph is disconnected");
			return;
		}
		
		if (Settings.get().getBoolean("output.exportModel")) {
			exportModel(solver, filename);
		}
		
		System.out.println("Solving " + filename + " with " + graph.getNodes().length + " nodes and " + graph.getEdges().length + " edges.");
		solver.getCplex().setParam(IntParam.MIPDisplay, 3);
		solver.solve();
		
		if (solver.isSolved()) {
			new Printer(solver).printSolution(false);
			new Verifier(solver).verify();
			
			if (Settings.get().getBoolean("output.exportSol")) {
				String solDir = Settings.get().getString("output.solutionDir");
				solver.getCplex().writeSolution(solDir + "run" + runId + ".sol");
			}
			
			System.out.println("Chromatic number is " + solver.getChi());
			System.out.println("Solved in " + solver.getTime() + " ticks");
		} else {
			System.out.println("Solution failed");
		}
		
		solver.end();
	}

	private static void exportModel(Solver solver, String filename) throws IloException {
		System.out.println("Exporting model to " + filename + ".lps");
		solver.getCplex().exportModel(filename + ".lps");
	}

}
