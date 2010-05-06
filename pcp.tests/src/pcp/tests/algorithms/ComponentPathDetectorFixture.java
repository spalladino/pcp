package pcp.tests.algorithms;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pcp.algorithms.data.ModelData;
import pcp.algorithms.data.StubModelData;
import pcp.algorithms.holes.ComponentHolesDetector;
import pcp.algorithms.iset.ComponentPathDetector;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.model.Model;
import pcp.solver.cuts.StubCutBuilder;
import pcp.solver.data.Iteration;
import props.Settings;
import exceptions.AlgorithmException;


public class ComponentPathDetectorFixture {

	PartitionedGraphBuilder builder;
	ComponentHolesDetector detector;
	PartitionedGraph graph;
	ModelData data;
	StubCutBuilder cuts;
	int colors;
	
	@BeforeClass
	public static void settings() throws Exception {
		Settings.load("test");
	}
	
	@Before
	public void setup() {
		builder = new PartitionedGraphBuilder("test");
		cuts = new StubCutBuilder();
	}
	
	@Test
	public void testK5() throws AlgorithmException {
		
		builder.createNodes(5);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
	
		colors = 3;
		data = new StubModelData(5).withWs(0.8, 1.0, 0.9)
			.withXs(0, 0.4, 0.5, 0.2)
			.withXs(1, 0.4, 0.5, 0.2)
			.withXs(2, 0.4, 0.5, 0.2)
			.withXs(3, 0.4, 0.5, 0.2)
			.withXs(4, 0.4, 0.5, 0.2);
	
		test();
	}
	
	@Test
	public void testK5PlusPath() throws AlgorithmException {
		
		builder.createNodes(7);
		
		builder.addEdge(0, 1)
			.addEdge(1, 2)
			.addEdge(2, 3)
			.addEdge(3, 4)
			.addEdge(4, 0)
			.addEdge(4, 5)
			.addEdge(5, 6);
	
		colors = 3;
		data = new StubModelData(7).withWs(0.8, 1.0, 1.0)
			.withXs(0, 0.4, 0.5, 0.2)
			.withXs(1, 0.4, 0.5, 0.4)
			.withXs(2, 0.4, 0.5, 0.9)
			.withXs(3, 0.4, 0.5, 0.1)
			.withXs(4, 0.4, 0.5, 0.1)
			.withXs(5, 0.4, 0.5, 0.9)
			.withXs(6, 0.4, 0.5, 0.6);
	
		test();
	}
	
	private void test() {
		PartitionedGraph graph = builder.getGraph();
		Model model = new Model(graph, colors);
		Iteration iter = new Iteration(model, data, cuts);
		ComponentPathDetector detector = new ComponentPathDetector(iter);
		detector.run();
		System.out.println("\nGraph with " + graph.N() + " nodes and " + graph.E() + " edges.");
		cuts.printIneqs(System.out);
	}
	
	
	
}
