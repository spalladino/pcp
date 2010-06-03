package pcp.interfaces;

import exceptions.AlgorithmException;

public interface IColorAssigner {
	
	void useColor(int node, int color) throws AlgorithmException;
	
}