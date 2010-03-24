package pcp;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.IntParam;
import pcp.algorithms.Preprocessor;
import pcp.algorithms.Verifier;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.connectivity.ConnectivityChecker;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.interfaces.IFactory;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.ModelBuilder;
import pcp.model.strategy.Coloring;
import pcp.solver.Kind;
import pcp.solver.Solver;
import pcp.solver.io.Printer;
import props.Settings;
public class Main {
	
	public static void main(String[] args) throws Exception {
		String filename, runId;
		Settings.load("run");
		
		filename = Settings.get().getPath("run.folder", "run.filename");
		runId = Settings.get().getString("run.id");
		
		solve(filename, runId);
		System.exit(0);
	}
	
	private static void solve(String filename, String runId) throws Exception {
		IFactory factory = Factory.get();
		BuilderStrategy strategy = BuilderStrategy.fromSettings();
		ExecutionData execution = new ExecutionData().withSettings();
		
		PartitionedGraph graph;
		Model model;
		ColoringAlgorithm coloring;
		
		PartitionedGraphBuilder builder = factory.getGraphBuilder(filename);
		Solver solver = factory.createSolver(Settings.get().getEnum("solver.kind", Kind.class));
		
		try {
			long initial = System.currentTimeMillis();
			graph = new Preprocessor(builder).preprocess().getGraph();
			coloring = Factory.get().coloring(strategy.getColoring(), graph);
			model = new ModelBuilder(graph, solver.getCplex()).buildModel(strategy, coloring); 
			solver.setModel(model);
			long elapsed = System.currentTimeMillis() - initial;
			execution.getData().put("preprocess.time", elapsed);
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
		
		final boolean useInitialSolution = Settings.get().getBoolean("start.useInitialSolution");
		if(useInitialSolution && Coloring.supportingAssignment.contains(model.getStrategy().getColoring())) {
			System.out.println("Using initial solution from coloring with value " + coloring.getChi());
			solver.loadInitialSolution(coloring);
		}
		
		System.out.println("Solving " + filename + " with " + graph.getNodes().length + " nodes and " + graph.getEdges().length + " edges.");
		solver.getCplex().setParam(IntParam.MIPDisplay, 3);
		solver.solve();
		
		if (solver.isSolved()) {
			new Printer(solver).printSolution(false);
			new Verifier(solver).verify();
			
			if (Settings.get().getBoolean("output.exportSol")) {
				String solDir = Settings.get().getString("output.solutionDir");
				solver.getCplex().writeSolution(solDir + "run." + runId + ".sol");
			}
			
			System.out.println("Chromatic number is " + solver.getChromaticNumber());
			System.out.println("Solved in " + solver.getTime() + " ticks");
			execution.getData().put("solution.success", true);
			solver.fillExecutionData(execution.getData());
		} else {
			System.out.println("Solution failed");
			execution.getData().put("solution.success", false);
		}
		
		execution.dump();
		solver.end();
	}

	private static void exportModel(Solver solver, String filename) throws IloException {
		System.out.println("Exporting model to " + filename + ".lps");
		solver.getCplex().exportModel(filename + ".lps");
	}

}
