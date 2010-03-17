package pcp.tests.algorithms;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import common.BoxInt;
import exceptions.AlgorithmException;

import pcp.algorithms.holes.HolesDetector;
import pcp.algorithms.holes.IHolesDetector.IHoleFilter;
import pcp.algorithms.holes.IHolesDetector.IHoleHandler;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.PartitionedGraphBuilder;


@SuppressWarnings("deprecation")
public class HolesDetectorFixture {

	PartitionedGraphBuilder builder;
	HolesDetector detector;
	
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
		}, HolesDetector.AllFilter);
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
		
		detector = new HolesDetector(builder.getGraph());
		detector.holes(handler, filter);
		
		Assert.assertEquals(expected, count.getData());
	}
	
	private void assertCount(int expected) throws AlgorithmException {
		assertCount(expected, HolesDetector.AllFilter);
	}
	
	private void assertHoles(IHoleHandler handler, IHoleFilter filter) throws AlgorithmException {
		System.out.println("Running...");
		detector = new HolesDetector(builder.getGraph());
		detector.holes(handler, filter);
	}
	
	
	
}
