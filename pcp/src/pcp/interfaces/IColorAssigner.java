package pcp.interfaces;

import exceptions.AlgorithmException;

public interface IColorAssigner {
	
	public static enum DSaturAssignment {
		CheckAdjs,
		Fast,
		Safe
	}
	
	void useColor(int node, int color) throws AlgorithmException;
	
}