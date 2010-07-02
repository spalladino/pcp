package pcp;

import pcp.algorithms.Preprocessor;
import pcp.algorithms.bounding.IterationsBounder;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.ColoringVerifier;
import pcp.algorithms.connectivity.ConnectivityChecker;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.interfaces.IFactory;
import pcp.model.BuilderStrategy;
import props.Settings;

public class Heuristic {

	public static void main(String[] args) throws Exception {
		String filename, runId;
		Settings.load("run");
		
		if (args.length >= 2) {
			filename = args[0];
			runId = args[1];
		} else {
			filename = Settings.get().getPath("run.folder", "run.filename");
			runId = Settings.get().getString("run.id");
		}
		
		solve(filename, runId);
		System.exit(0);
	}
	
	public static void solve(String filename, String runId) throws Exception {
		IFactory factory = Factory.get();
		BuilderStrategy strategy = BuilderStrategy.fromSettings();

		for (PartitionedGraphBuilder builder : factory.getGraphBuilders(filename, Integer.MAX_VALUE)) {
			System.out.println("Applying heuristic to " + filename);
			ExecutionData data = new ExecutionData().withProblemSettings();
			data.withOriginalInputData(builder);
			
			PartitionedGraph graph = new Preprocessor(builder).preprocess().getGraph();
			new ConnectivityChecker(graph).raiseIfUnconnected();
			
			data.withInputData(graph);
			ColoringAlgorithm solver = factory
				.coloring(strategy.getColoring(), graph)
				.withBounder(new IterationsBounder("coloring.full"));
		
			new ColoringVerifier(graph).verify(solver);
			
			System.out.println("Using solver type " + solver.getClass().getName());
			System.out.println("Chromatic number is " + solver.getChi());
			System.out.println("Solved in " + solver.getBounder().getMillis() + " ticks");
			System.out.println();
			
			data.getData().put("solution.time", (double)solver.getBounder().getMillis() / 1000.0);
			data.getData().put("solution.chi", solver.getChi());
			data.getData().put("solution.found", (double)solver.getBounder().getLastImproved() / 1000.0);
			data.dump();
		}
	}


}
