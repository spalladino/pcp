package pcp;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.IntParam;

import java.util.List;

import pcp.algorithms.Preprocessor;
import pcp.algorithms.Verifier;
import pcp.algorithms.bounding.IterationsBounder;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.connectivity.ConnectivityChecker;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.entities.simple.Node;
import pcp.interfaces.IFactory;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.ModelBuilder;
import pcp.model.strategy.Coloring;
import pcp.solver.Kind;
import pcp.solver.Solver;
import pcp.solver.io.Printer;
import pcp.utils.GraphUtils;
import props.Settings;
import exceptions.AlgorithmException;
public class Main {
	
	public static void main(String[] args) throws Exception {
		String filename, runId;
		Settings.load("run");
		
		if (Settings.get().getEnum("solver.kind", Kind.class).equals(Kind.Heuristic)) {
			Heuristic.main(args);
			System.exit(0);
			return;
		}
		
		filename = Settings.get().getPath("run.folder", "run.filename");
		runId = Settings.get().getString("run.id");
		
		solve(filename, runId);
		System.exit(0);
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
			build(strategy, execution, builder, solver);
		} catch (Exception e) {
			System.err.println("Error creating model: " + e.getMessage());
			e.printStackTrace(System.err);
			return;
		}

		if (!new ConnectivityChecker(graph).check()) {
			System.err.println("Graph is disconnected");
			return;
		}
		
		if (Settings.get().getBoolean("output.exportModel")) {
			exportModel(solver, filename);
		}
		
		final boolean useInitialSolution = Settings.get().getBoolean("solver.useInitialSolution");
		if(useInitialSolution && Coloring.supportingAssignment.contains(model.getStrategy().getColoring())) {
			System.out.println("Using initial solution from coloring with value " + coloring.getChi());
			solver.loadInitialSolution(coloring);
		}
		
		System.out.println("Solving " + filename + " with " + graph.P() + " partitions, " + graph.N() + " nodes and " + graph.E() + " edges.");
		execution.withInputData(graph);
		
		solver.getCplex().setParam(IntParam.MIPDisplay, Settings.get().getInteger("logging.mipdisplay"));
		solver.getCplex().setParam(IntParam.MIPInterval, Settings.get().getInteger("logging.mipinterval"));
		
		solver.solve();
		solver.fillExecutionData(execution.getData());
		
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

	private static void build(BuilderStrategy strategy, ExecutionData execution, PartitionedGraphBuilder builder,
			Solver solver) throws IloException, AlgorithmException {
		execution.withOriginalInputData(builder);
		long initial = System.currentTimeMillis();
		
		// Preprocess graph
		Preprocessor preprocessor = new Preprocessor(builder);
		graph = preprocessor.preprocess().getGraph();
		List<Node> clique = preprocessor.getClique();
		
		// Make initial coloring
		coloring = Factory.get().coloring(strategy.getColoring(), graph).withBounder(new IterationsBounder("coloring.initial"));
		if (clique != null) coloring.setInitialClique(clique);
		
		// Build model
		model = new ModelBuilder(graph, solver.getCplex()).buildModel(strategy, coloring, clique); 
		solver.setModel(model);
		
		// Log elapsed time
		long elapsed = System.currentTimeMillis() - initial;
		execution.getData().put("preprocess.time", elapsed);
	}

	private static void exportModel(Solver solver, String filename) throws IloException {
		System.out.println("Exporting model to " + filename + ".lps");
		solver.getCplex().exportModel(filename + ".lps");
	}

}
