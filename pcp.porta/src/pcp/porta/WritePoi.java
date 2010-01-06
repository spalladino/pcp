package pcp.porta;

import ilog.concert.IloException;
import pcp.Factory;
import pcp.entities.PartitionedGraph;
import pcp.entities.PartitionedGraphBuilder;
import pcp.model.BuilderStrategy;
import pcp.porta.io.PoiWriter;
import pcp.porta.poi.PointsGenerator;
import pcp.porta.processing.Cardinals;


public class WritePoi {
	
	public static void write(String filename, String output, BuilderStrategy strategy) throws Exception, IloException {
		PartitionedGraphBuilder graphb = Factory.get().getGraphBuilder(filename);
		System.out.println("Graph has " + graphb.getNodes().length + " nodes and " + graphb.getEdges().length + " edges.");
		
		PartitionedGraph graph = graphb.getGraph();
		
		Cardinals cardinals = Cardinals.fromPortaSettings();
		
		PointsGenerator generator = new PointsGenerator(graph, cardinals, strategy);
		PoiWriter writer = new PoiWriter(generator.getDimension(), generator.getPoints());
		writer.write(output);
		
		System.out.println("Poi representation generated in " + output);
	}
	
}
