package pcp.interfaces;

import java.util.List;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.common.sorting.NodeColorValueComparator;
import pcp.entities.SortedPartitionedGraph;
import pcp.model.Model;


public interface IAlgorithmSource extends IModelData, IAlgorithmBounder {

	SortedPartitionedGraph getSortedGraph(int color, boolean asc);
	
	NodeColorValueComparator getNodeComparator(int color, boolean asc);
	
	List<Integer> getSortedColors(boolean asc, boolean includeZeros, boolean includeOnes);
	
	Model getModel();
	
}
