package pcp.interfaces;

import java.util.Comparator;
import java.util.List;

import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.partitioned.SortedPartitionedGraph;


public interface ISortedProvider {
	
	SortedPartitionedGraph getSortedGraph(int color, boolean asc);
	
	List<Integer> getSortedFractionalColors(boolean asc);
	
	List<Integer> getSortedColors(boolean asc);

	List<Integer> getSortedColors(boolean asc, boolean onlyFrac);
	
	Comparator<Edge> getEdgeComparator(int color, boolean asc);
	
	Comparator<Partition> getPartitionComparator(int color, boolean asc);

	Comparator<Node> getNodeComparator(int color, boolean asc);
	
}
