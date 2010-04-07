package pcp.interfaces;

import java.util.List;

import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;

public interface ICutBuilder {

	void addClique(List<Node> nodes, int color);
	
	void addHole(List<Node> nodes, int color);

	void addPath(List<Node> path, int color);
	
	void addBlockColor(Partition partition, int j0);

	void addGPrimeHole(List<pcp.entities.simple.Node> hole, int color);
	
}
