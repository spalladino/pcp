package pcp.porta;

import pcp.Factory;
import pcp.entities.PartitionedGraphBuilder;
import pcp.model.BuilderStrategy;
import pcp.porta.io.ModelWriter;
import pcp.porta.io.PortaReader;
import pcp.porta.model.Model;
import pcp.porta.processing.Cardinals;
import pcp.porta.processing.Processor;


public class ProcessIeq {

	public static void process(String graph, String input, String output, BuilderStrategy strategy) throws Exception {		
		PartitionedGraphBuilder graphb = Factory.get().getGraphBuilder(graph);
		Cardinals c = Cardinals.fromPortaSettings(graphb.N());
		
		Model model = new Model(c);
		PortaReader reader = new PortaReader(model);
		reader.read(input);
		
		System.out.println("Parsed input from " + input);
		
		Processor processor = new Processor(graphb, c, strategy);
		processor.process(model);
		
		System.out.println("Processed input model");
		
		ModelWriter writer = new ModelWriter(model);
		writer.write(output);
		
		System.out.println("Generated output in " + output);
	}
	
}
