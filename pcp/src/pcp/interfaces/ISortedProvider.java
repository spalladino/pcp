package pcp.interfaces;

import java.util.List;

import pcp.common.sorting.NodeColorValueComparator;
import pcp.entities.SortedPartitionedGraph;


public interface ISortedProvider {
	
	SortedPartitionedGraph getSortedGraph(int color, boolean asc);
	
	NodeColorValueComparator getNodeComparator(int color, boolean asc);
	
	List<Integer> getSortedFractionalColors(boolean asc);
	
	List<Integer> getSortedColors(boolean asc);
	
}
