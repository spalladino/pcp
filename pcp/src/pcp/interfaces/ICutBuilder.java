package pcp.interfaces;

import java.util.List;

import pcp.entities.Node;
import pcp.entities.Partition;

public interface ICutBuilder {

	void addClique(List<Node> nodes, int color);
	
	void addHole(List<Node> nodes, int color);

	void addBlockColor(Partition partition, int j0);
	
}
