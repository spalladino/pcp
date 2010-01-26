package pcp.tests.algorithms;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pcp.Settings;
import pcp.algorithms.clique.ExtendedCliqueDetector;
import pcp.algorithms.data.ModelData;
import pcp.algorithms.data.StubModelData;
import pcp.entities.PartitionedGraph;
import pcp.entities.PartitionedGraphBuilder;
import pcp.model.Model;
import pcp.solver.cuts.StubCutBuilder;
import pcp.solver.data.Iteration;


public class ExtendedCliqueFixture {

	PartitionedGraphBuilder builder;
	StubCutBuilder cuts;
	ModelData data;

	int colors;
	
	@BeforeClass
	public static void loadSettings() throws Exception {
		Settings.load("test");
	}
	
	@Before
	public void setup() {
		builder = new PartitionedGraphBuilder("test");
		cuts = new StubCutBuilder();
	}
	
	@Test
	public void testK3() {
		builder.createNodes(3);
		builder.addEdge(0, 1).addEdge(1, 2).addEdge(2, 0);
		colors = 3;
		
		data = new StubModelData(3).withWs(0.8, 1.0, 0.7)
			.withXs(0, 0.4, 0.5, 0.4)
			.withXs(1, 0.4, 0.5, 0.4)
			.withXs(2, 0.4, 0.5, 0.4);
		
		test();
	}

	@Test
	public void testK4() {
		builder.createNodes(4)
			.addEdge(0, 1).addEdge(1, 2).addEdge(2, 0)
			.addEdge(3, 0).addEdge(3, 1).addEdge(3, 2);
		
		colors = 4;
		
		data = new StubModelData(4).withWs(0.8, 1.0, 0.7, 0.3)
			.withXs(0, 0.4, 0.5, 0.4, 0.1)
			.withXs(1, 0.4, 0.5, 0.0, 0.2)
			.withXs(2, 0.4, 0.5, 0.2, 0.1)
			.withXs(3, 0.4, 0.5, 0.1, 0.0);
		
		test();
	}
	
	@Test
	public void testD4() {
		builder.createNodes(4)
			.addEdge(0, 1).addEdge(1, 2).addEdge(2, 0)
			.addEdge(3, 1).addEdge(3, 2);
		
		colors = 4;
		
		data = new StubModelData(4).withWs(0.8, 1.0, 0.7, 0.3)
			.withXs(0, 0.4, 0.5, 0.4, 0.2)
			.withXs(1, 0.4, 0.5, 0.1, 0.1)
			.withXs(2, 0.4, 0.5, 0.2, 0.1)
			.withXs(3, 0.4, 0.5, 0.4, 0.1);
		
		test();
	}
	
	@Test
	public void testG5() {
		builder.createNodes(5)
			.addEdge(0, 1).addEdge(1, 2).addEdge(2, 0)
			.addEdge(3, 1).addEdge(3, 2)
			.addEdge(3, 4);
		
		colors = 4;
		
		data = new StubModelData(5).withWs(0.8, 1.0, 0.7, 0.3)
			.withXs(0, 0.4, 0.5, 0.4, 0.2)
			.withXs(1, 0.4, 0.5, 0.1, 0.1)
			.withXs(2, 0.4, 0.5, 0.2, 0.1)
			.withXs(3, 0.4, 0.5, 0.4, 0.1)
			.withXs(4, 0.4, 0.5, 0.3, 0.3);
		
		test();
	}
	
	private void test() {
		PartitionedGraph graph = builder.getGraph();
		Model model = new Model(graph, colors);
		Iteration iter = new Iteration(model, data, cuts);
		ExtendedCliqueDetector detector = new ExtendedCliqueDetector(iter);
		detector.run();
		
		cuts.printIneqs(System.out);
	}
	
}
