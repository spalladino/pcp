package pcp.entities.partitioned;

import pcp.entities.IPartitionedGraph;
import pcp.entities.simple.SimpleGraphBuilder;
import pcp.utils.GraphUtils;


public class GPrimeBuilder extends SimpleGraphBuilder {

	public GPrimeBuilder(String name) {
		super(name);
	}
	
	public GPrimeBuilder(IPartitionedGraph graph) {
		super(graph.getName());

		Partition[] ps = graph.getPartitions();
		
		for (int i = 0; i < ps.length; i++) {
			this.addNode(i);
		}
		
		for (int i = 0; i < ps.length; i++) {
			for (int j = i+1; j < ps.length; j++) {
				if (GraphUtils.areBipartite(ps[i], ps[j])) {
					this.addEdge(i, j);
				}
			}
		}
	}
	
	
}
