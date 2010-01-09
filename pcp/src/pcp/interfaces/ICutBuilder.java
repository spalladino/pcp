package pcp.interfaces;

import java.util.List;

import pcp.entities.Node;

public interface ICutBuilder {

	void addClique(List<Node> nodes, int color);
	
}
