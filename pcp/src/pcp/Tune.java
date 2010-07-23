package pcp;

import ilog.cplex.IloCplex.IntParam;
import pcp.algorithms.Verifier;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.connectivity.ConnectivityChecker;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.interfaces.IFactory;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.strategy.Coloring;
import pcp.solver.Kind;
import pcp.solver.Solver;
import pcp.solver.io.Printer;
import props.Settings;



public class Tune {
	
	public static void main(String[] args) throws Exception {
		
		
	}
	

	private static PartitionedGraph graph;
	private static Model model;
	private static ColoringAlgorithm coloring;
	
	private static void solve(String filename, String runId) throws Exception {
		IFactory factory = Factory.get();
		BuilderStrategy strategy = BuilderStrategy.fromSettings();
		ExecutionData execution = new ExecutionData().withProblemSettings();
		
		PartitionedGraphBuilder builder = factory.getGraphBuilder(filename);
		Solver solver = factory.createSolver(Settings.get().getEnum("solver.kind", Kind.class));
		
		try {
			Main.build(strategy, execution, builder, solver);
		} catch (Exception e) {
			System.err.println("Error creating model: " + e.getMessage());
			e.printStackTrace(System.err);
			return;
		}
		
		final boolean useInitialSolution = Settings.get().getBoolean("solver.useInitialSolution");
		if(useInitialSolution && Coloring.supportingAssignment.contains(model.getStrategy().getColoring())) {
			System.out.println("Using initial solution from coloring with value " + coloring.getChi());
			solver.loadInitialSolution(coloring);
		}
		
		System.out.println("Solving " + filename + " with " + graph.P() + " partitions, " + graph.N() + " nodes and " + graph.E() + " edges.");
		System.out.println("GPrime has " + graph.getGPrime().N() + " nodes and " + graph.getGPrime().E() + " edges.");
		execution.withInputData(graph);
		
		solver.getCplex().setParam(IntParam.MIPDisplay, Settings.get().getInteger("logging.mipdisplay"));
		solver.getCplex().setParam(IntParam.MIPInterval, Settings.get().getInteger("logging.mipinterval"));
		
		solver.solve();
		solver.fillData(execution.getData());
		
		if (solver.isSolved()) {
			if (Settings.get().getBoolean("logging.solution")) {
				new Printer(solver).printSolution(false);
			}
			
			new Verifier(solver).verify();
			
			if (Settings.get().getBoolean("output.exportSol")) {
				String solDir = Settings.get().getString("output.solutionDir");
				solver.getCplex().writeSolution(solDir + "run." + runId + ".sol");
			}
			
			System.out.println("Chromatic number is " + solver.getChromaticNumber());
			System.out.println("Gap is " + solver.getGap());
			System.out.println("Solved in " + solver.getTime() + " seconds");			
		} else {
			System.out.println("Solution failed after " + solver.getTime());
		}
		
		execution.dump();
		solver.end();
	}
}
