package pcp.interfaces;

import ilog.concert.IloException;

import java.util.List;

import pcp.entities.IPartitionedGraph;

public interface ISolutionData extends IModelData {

	IPartitionedGraph getGraph();
	
	int getNodeColor(int node) throws IloException;

	List<Integer> getColorsUsed() throws IloException;
	
}
