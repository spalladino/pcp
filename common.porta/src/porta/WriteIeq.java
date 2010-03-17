package porta;

import ilog.concert.IloException;
import porta.interfaces.IEntity;
import porta.interfaces.IFactory;
import porta.io.PortaWriter;
import porta.model.Model;
import porta.model.ilog.MockMPModeler;
import porta.processing.ITranslator;


public class WriteIeq {
	
	IFactory factory;
	
	public WriteIeq(IFactory factory) {
		this.factory = factory;
	}

	public void write(String filename, String output, Boolean preprocess) throws Exception, IloException {
		IEntity graph = factory.readEntity(filename, preprocess);
		
		Model model = factory.generateModel(graph);
		
		PortaWriter writer = factory.createPortaWriter(model);
		writer.write(output);
		
		System.out.println("Inequalities representation generated in " + output);
	}
	
}
