package porta;

import ilog.concert.IloException;
import porta.interfaces.IFactory;
import porta.io.PoiWriter;
import porta.poi.IPointsGenerator;

@SuppressWarnings("unchecked")
public class WritePoi {
	
	IFactory factory;
	
	public WritePoi(IFactory factory) {
		this.factory = factory;
	}
	
	public void write(String filename, String output, Boolean preprocess) throws Exception, IloException {
		Object graph = factory.readEntity(filename, preprocess);
		
		IPointsGenerator generator = factory.createPointsGenerator(graph);
		PoiWriter writer = new PoiWriter(generator.getDimension(), generator.getPoints());
		writer.write(output);
		
		System.out.println("Poi representation generated in " + output);
	}
	
}
