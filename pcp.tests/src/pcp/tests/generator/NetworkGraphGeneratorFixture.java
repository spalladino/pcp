package pcp.tests.generator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pcp.generator.DimacsPartitionedGraph;
import pcp.generator.GraphProperties;
import pcp.generator.network.Lightpath;
import pcp.generator.network.NetworkGraphBuilder;
import pcp.generator.network.Lightpath.Route;

public class NetworkGraphGeneratorFixture {

	DimacsPartitionedGraph graph;
	
	@Test
	public void testSinglePathNoConflicts() throws IOException {
		Lightpath path = new Lightpath()
			.withRoute(new Route().withNodes(1,2,3,4,5))
			.withRoute(new Route().withNodes(1,6,7,8,5))
			.withRoute(new Route().withNodes(1,9,5));
	
		buildGraph(path);
		assertGraphSize(3, 1, 0);
		assertPartition(0, 0,1,2);
	}
	
	@Test
	public void testSinglePathInvertedConflict() throws IOException {
		Lightpath path = new Lightpath()
			.withRoute(new Route().withNodes(1,2,3,4,5))
			.withRoute(new Route().withNodes(1,4,3,2,5))
			.withRoute(new Route().withNodes(1,3,5));

		buildGraph(path);
		
		assertGraphSize(3, 1, 1);
		assertPartition(0, 0,1,2);
	}

	@Test
	public void testSinglePathWithConflicts() throws IOException {
		Lightpath path = new Lightpath()
			.withRoute(new Route().withNodes(1,2,3,4,5))
			.withRoute(new Route().withNodes(1,6,7,8,5))
			.withRoute(new Route().withNodes(1,2,3,8,5));

		buildGraph(path);
		assertGraphSize(3, 1, 2);
		assertPartition(0, 0,1,2);
	}
	
	@Test
	public void testMultiplePaths() throws IOException {
		Lightpath path1 = new Lightpath()
			.withRoute(new Route().withNodes(1,2,3,4,5))
			.withRoute(new Route().withNodes(1,6,7,8,5));
		
		Lightpath path2 = new Lightpath()
			.withRoute(new Route().withNodes(2,3,9,8))
			.withRoute(new Route().withNodes(2,4,7,8));
		
		Lightpath path3 = new Lightpath()
			.withRoute(new Route().withNodes(5,2,3,9));	

		buildGraph(path1, path2, path3);
		
		assertGraphSize(5, 3, 4);
		assertPartition(0, 0,1);
		assertPartition(1, 2,3);
		assertPartition(2, 4);
	}
	
	public void assertGraphSize(int nodes, int partitions, int edges) {
		Assert.assertEquals(partitions, graph.getPartitionsList().size());
		Assert.assertEquals(edges, graph.getEdgeList().size());
		Assert.assertEquals(nodes, graph.getNodes());
	}
	
	public void assertPartition(int partitionIndex, Integer... nodes) {
		List<Integer> actual = graph.getPartitionsList().get(partitionIndex).getNodes();
		Assert.assertArrayEquals(nodes, (Integer[]) actual.toArray(new Integer[actual.size()]));
	}
	
	public void buildGraph(Lightpath... paths) throws IOException {
		GraphProperties props = new GraphProperties();
		NetworkGraphBuilder builder = new NetworkGraphBuilder(props);
		graph = builder.buildGraph(Arrays.asList(paths));
		
		OutputStreamWriter writer = new OutputStreamWriter(System.out);
		graph.write(writer);
		writer.close();
	}
	
}
