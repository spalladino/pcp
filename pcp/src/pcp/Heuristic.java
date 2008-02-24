package pcp;

import pcp.algorithms.Preprocessor;
import pcp.algorithms.coloring.Coloring;
import pcp.entities.PartitionedGraph;
import pcp.entities.PartitionedGraphBuilder;
import pcp.interfaces.IFactory;
import pcp.model.BuilderStrategy;

public class Heuristic {

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

		PartitionedGraphBuilder builder = factory.getGraphBuilder(filename);
		PartitionedGraph graph = new Preprocessor(builder).preprocess().getGraph();
		Coloring solver = factory.coloring(strategy.getColoring(), graph);
			
		System.out.println("Using solver type " + solver.getClass().getName());
		System.out.println("Chromatic number is " + solver.getChi());
		System.out.println("Solved in " + solver.getBounder().getMillis() + " ticks");
	}


}
