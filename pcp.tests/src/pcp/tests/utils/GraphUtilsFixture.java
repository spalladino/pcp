package pcp.tests.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.utils.GraphUtils;

public class GraphUtilsFixture {

	private PartitionedGraphBuilder builder;

	@Before
	public void setup() {
		builder = new PartitionedGraphBuilder("test");
	}
	
	@Test
	public void testGroupByPartition() {
		builder.addNode(0, 0);
		builder.addNode(1, 0);
		builder.addNode(2, 0);
		builder.addNode(3, 0);
		builder.addNode(4, 1);
		builder.addNode(5, 1);
		builder.addNode(6, 2);
		builder.addNode(7, 2);
		builder.addNode(8, 3);
		builder.addNode(9, 4);
		builder.addNode(10, 4);
		builder.addNode(11, 4);
		builder.addNode(12, 5);
		
		Map<Partition, List<Node>> groupByPartition = GraphUtils.groupByPartition(builder.getNodes());
		Assert.assertEquals(6, groupByPartition.size());
		
		List<Node> nodes;
		
		nodes = groupByPartition.get(builder.getPartitions()[0]);
		Assert.assertEquals(0, nodes.get(0).index());
		Assert.assertEquals(1, nodes.get(1).index());
		Assert.assertEquals(2, nodes.get(2).index());
		Assert.assertEquals(3, nodes.get(3).index());
		
		nodes = groupByPartition.get(builder.getPartitions()[1]);
		Assert.assertEquals(4, nodes.get(0).index());
		Assert.assertEquals(5, nodes.get(1).index());
		
		nodes = groupByPartition.get(builder.getPartitions()[2]);
		Assert.assertEquals(6, nodes.get(0).index());
		Assert.assertEquals(7, nodes.get(1).index());
		
		nodes = groupByPartition.get(builder.getPartitions()[3]);
		Assert.assertEquals(8, nodes.get(0).index());
		
		nodes = groupByPartition.get(builder.getPartitions()[4]);
		Assert.assertEquals(9, nodes.get(0).index());
		Assert.assertEquals(10, nodes.get(1).index());
		Assert.assertEquals(11, nodes.get(2).index());
		
		nodes = groupByPartition.get(builder.getPartitions()[5]);
		Assert.assertEquals(12, nodes.get(0).index());
	}
	
	@Test
	public void testGroupSomeByPartition() {
		builder.addNode(0, 0);
		builder.addNode(1, 0);
		builder.addNode(2, 0);
		
		builder.addNode(3, 0);
		builder.addNode(4, 1);
		builder.addNode(5, 1);
		builder.addNode(6, 2);
		
		builder.addNode(7, 2);
		builder.addNode(8, 3);
		builder.addNode(9, 4);
		builder.addNode(10, 4);
		builder.addNode(11, 4);
		builder.addNode(12, 5);
		
		Map<Partition, List<Node>> groupByPartition = GraphUtils.groupByPartition(Arrays.copyOfRange(builder.getNodes(), 3, 7));
		Assert.assertEquals(3, groupByPartition.size());
		
		List<Node> nodes;
		
		nodes = groupByPartition.get(builder.getPartitions()[0]);
		Assert.assertEquals(3, nodes.get(0).index());
		
		nodes = groupByPartition.get(builder.getPartitions()[1]);
		Assert.assertEquals(4, nodes.get(0).index());
		Assert.assertEquals(5, nodes.get(1).index());
		
		nodes = groupByPartition.get(builder.getPartitions()[2]);
		Assert.assertEquals(6, nodes.get(0).index());
	}
	
}
