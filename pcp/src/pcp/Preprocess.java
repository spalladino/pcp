package pcp;

import pcp.algorithms.Preprocessor;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.interfaces.IFactory;
import props.Settings;


public class Preprocess {
	
	public static void main(String[] args) throws Exception {
		String filename = Settings.get().getPath("run.folder", "run.filename");
		
		IFactory factory = Factory.get();
		ExecutionData execution = new ExecutionData().withProblemSettings();
		
		PartitionedGraphBuilder builder = factory.getGraphBuilder(filename);
		execution.withOriginalInputData(builder);
		
		// Preprocess graph
		Preprocessor preprocessor = new Preprocessor(builder);
		preprocessor.preprocess().getGraph();
		preprocessor.getClique();
		preprocessor.fillData(execution.getData());
		
		execution.withInputData(builder);
		
		execution.dump();
	}
	
}
