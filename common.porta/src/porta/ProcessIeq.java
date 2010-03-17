package porta;

import porta.base.BaseParameters;
import porta.interfaces.IFactory;
import porta.io.ModelWriter;
import porta.io.PortaReader;
import porta.model.BaseModel;
import porta.processing.IProcessor;

@SuppressWarnings("unchecked")
public class ProcessIeq {

	IFactory factory;
	
	public ProcessIeq(IFactory factory) {
		this.factory = factory;
	}

	public void process(String graphfile, String input, String output) throws Exception {		
		BaseParameters c = factory.getParameters();
		BaseModel model = factory.createModel(c);
		PortaReader reader = new PortaReader(model, factory.createTranslator());
		reader.read(input);
		
		System.out.println("Parsed input from " + input);
		
		Object graph = factory.readEntity(graphfile, false);
		IProcessor processor = factory.createProcessor(graph, c);
		processor.process(model);
		
		System.out.println("Processed input model");
		
		ModelWriter writer = factory.createModelWriter(model);
		writer.write(output);
		
		System.out.println("Generated output in " + output);
	}
	
}
