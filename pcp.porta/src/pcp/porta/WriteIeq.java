package pcp.porta;

import ilog.concert.IloException;
import pcp.Factory;
import pcp.algorithms.Preprocessor;
import pcp.entities.PartitionedGraph;
import pcp.entities.PartitionedGraphBuilder;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.ModelBuilder;
import pcp.porta.io.PortaWriter;
import pcp.porta.model.ilog.MockMPModeler;
import pcp.porta.processing.Cardinals;
import pcp.porta.processing.Translator;


public class WriteIeq {
	
	public static void write(String filename, String output, Boolean preprocess, BuilderStrategy strategy) throws Exception, IloException {
		PartitionedGraphBuilder graphb = Factory.get().getGraphBuilder(filename);
		if (preprocess) { 
			graphb = new Preprocessor(graphb).preprocess();
			System.out.println("Preprocessed graph has " + graphb.getNodes().length + " nodes and " + graphb.getEdges().length + " edges.");
		}  else {
			System.out.println("Graph has " + graphb.getNodes().length + " nodes and " + graphb.getEdges().length + " edges.");
		}
		
		PartitionedGraph graph = graphb.getGraph();
		
		MockMPModeler modeler = new MockMPModeler();
		ModelBuilder builder = new ModelBuilder(graph, modeler);
		Model model = builder.buildModel(strategy);
		Cardinals cardinals = new Cardinals(model.getNodeCount(), model.getColorCount());
		Translator translator = new Translator(cardinals);
		
		PortaWriter writer = new PortaWriter(modeler.asModel(translator));
		writer.write(output);
		
		System.out.println("Inequalities representation generated in " + output);
	}
	
}
