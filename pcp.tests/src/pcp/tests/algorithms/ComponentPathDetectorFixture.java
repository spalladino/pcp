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
	public void testFoo() throws AlgorithmException {
		
		builder.createNodes(5);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
	
		colors = 3;
		data = new StubModelData(5).withWs(0.8, 1.0, 0.7)
			.withXs(0, 0.4, 0.5, 0.4)
			.withXs(1, 0.4, 0.5, 0.4)
			.withXs(2, 0.4, 0.5, 0.4)
			.withXs(3, 0.4, 0.5, 0.4)
			.withXs(4, 0.4, 0.5, 0.4);
	
		test();
	}
	
	private void test() {
		PartitionedGraph graph = builder.getGraph();
		Model model = new Model(graph, colors);
		Iteration iter = new Iteration(model, data, cuts);
		ComponentPathDetector detector = new ComponentPathDetector(iter);
		detector.run();
		
		cuts.printIneqs(System.out);
	}
	
	
	
}
