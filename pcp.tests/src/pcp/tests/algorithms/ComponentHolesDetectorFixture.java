package pcp.tests.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pcp.Settings;
import pcp.algorithms.AlgorithmException;
import pcp.algorithms.holes.ComponentHolesDetector;
import pcp.algorithms.holes.IHolesDetector;
import pcp.algorithms.holes.IHolesDetector.IHoleFilter;
import pcp.algorithms.holes.IHolesDetector.IHoleHandler;
import pcp.common.BoxInt;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;


public class ComponentHolesDetectorFixture {

	PartitionedGraphBuilder builder;
	ComponentHolesDetector detector;
	private PartitionedGraph graph;
	
	@BeforeClass
	public static void settings() throws Exception {
		Settings.load("test");
	}
	
	@Before
	public void setup() {
		builder = new PartitionedGraphBuilder("test");
	}
	
	@Test
	public void shouldNotDetectAnything() throws AlgorithmException {
		builder.createNodes(5);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		
		builder.addEdge(1, 3);
		
		assertCount(0);
	}
	
	@Test
	public void shouldDetectSingle() throws AlgorithmException {
		builder.createNodes(5);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);

		assertCount(1);
	}
	
	@Test
	public void shouldDetectSingleShuffled() throws AlgorithmException {
		builder.createNodes(7);
		
		builder.addEdge(5, 4);
		builder.addEdge(1, 0);
		builder.addEdge(3, 4);
		builder.addEdge(6, 0);
		builder.addEdge(3, 2);
		builder.addEdge(5, 6);
		builder.addEdge(1, 2);

		assertCount(1);
	}

	@Test
	public void shouldDetectBothOverlapping() throws AlgorithmException {
		builder.createNodes(6);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		
		builder.addEdge(5, 0);
		builder.addEdge(5, 2);

		assertCount(2);
	}
	
	@Test
	public void shouldDetectBothDisjoint() throws AlgorithmException {
		builder.createNodes(9);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		
		builder.addEdge(2, 6);
		builder.addEdge(6, 7);
		builder.addEdge(7, 8);
		builder.addEdge(8, 5);
		builder.addEdge(5, 2);

		assertCount(2);
	}
	
	@Test
	public void shouldDetectBothDifferent() throws AlgorithmException {
		builder.createNodes(10);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		
		builder.addEdge(2, 6);
		builder.addEdge(6, 7);
		builder.addEdge(7, 9);
		builder.addEdge(9, 8);
		builder.addEdge(8, 5);
		builder.addEdge(5, 2);

		assertCount(2);
	}
	
	@Test
	public void shouldDetectOnlyComponent() throws AlgorithmException {
		builder.addNode(0, 0);
		builder.addNode(1, 1);
		builder.addNode(2, 2);
		builder.addNode(3, 3);
		builder.addNode(4, 4);
		
		builder.addNode(5, 5);
		builder.addNode(6, 6);
		builder.addNode(7, 7);
		builder.addNode(8, 2);
		builder.addNode(9, 8);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		
		builder.addEdge(2, 6);
		builder.addEdge(6, 7);
		builder.addEdge(7, 9);
		builder.addEdge(9, 8);
		builder.addEdge(8, 5);
		builder.addEdge(5, 2);

		assertCount(1);
		
		assertNotHole(0, 1, 2, 3, 4, 5);
		assertNotHole(0, 1, 2, 3, 4, 5, 0);
		assertNotHole(0, 1, 2, 3);
		assertNotHole(0, 2, 3, 4);
		assertNotHole(2, 5, 8, 9, 7, 6);
		
		assertHole(0, 1, 2, 3, 4);
	}
	
	@Test
	public void shouldDetectOnlyOdd() throws AlgorithmException {
		builder.createNodes(10);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		
		builder.addEdge(2, 6);
		builder.addEdge(6, 7);
		builder.addEdge(7, 9);
		builder.addEdge(9, 8);
		builder.addEdge(8, 5);
		builder.addEdge(5, 2);

		IHoleFilter odds = new IHoleFilter() {
			public boolean use(List<Node> hole) {
				return hole.size() % 2 == 1;
			}
		};
		
		assertCount(1, odds);
	}

	@Test
	public void shouldStopWhenHandlerStops() throws AlgorithmException {
		builder.createNodes(10);
		
		builder.addEdge(0, 1);
		builder.addEdge(1, 2);
		builder.addEdge(2, 3);
		builder.addEdge(3, 4);
		builder.addEdge(4, 0);
		
		builder.addEdge(2, 6);
		builder.addEdge(6, 7);
		builder.addEdge(7, 9);
		builder.addEdge(9, 8);
		builder.addEdge(8, 5);
		builder.addEdge(5, 2);

		assertHoles(new IHoleHandler() {
			public boolean handle(List<Node> hole) {
				Assert.assertEquals(5, hole.size());
				return false;
			}
		}, IHolesDetector.AllFilter);
	}
	
	private void assertCount(int expected, IHoleFilter filter) throws AlgorithmException {
		System.out.println("Running...");
		final BoxInt count = new BoxInt(0);
		IHoleHandler handler = new IHoleHandler() {
			public boolean handle(List<Node> hole) {
				count.incData();
				return true;
			}
		};
		
		graph = builder.getGraph();
		detector = new ComponentHolesDetector(graph);
		detector.holes(handler, filter);
		
		Assert.assertEquals(expected, count.getData());
	}
	
	private void assertCount(int expected) throws AlgorithmException {
		assertCount(expected, IHolesDetector.AllFilter);
	}
	
	private void assertHoles(IHoleHandler handler, IHoleFilter filter) throws AlgorithmException {
		System.out.println("Running...");
		detector = new ComponentHolesDetector(builder.getGraph());
		detector.holes(handler, filter);
	}
	
	private void assertNotHole(Integer... nodes) {
		assertIsHole(false, nodes);
	}
	
	private void assertHole(Integer... nodes) {
		assertIsHole(true, nodes);
	}
	
	private void assertIsHole(boolean isHole, Integer... nodes) {
		List<Node> list = new ArrayList<Node>(nodes.length);
		for(Integer node : nodes) {
			list.add(graph.getNode(node));
		}
		
		if (isHole) {
			Assert.assertTrue(detector.checkHole(list));
		} else {
			Assert.assertFalse(detector.checkHole(list));
		}
	}
	
	
	
}
