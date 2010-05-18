package pcp.solver.branching;

public class PcpNodeData {

	final private int depth;
	
	final private int fixedNodeColors;

	public int getDepth() {
		return depth;
	}

	public int getFixedNodeColors() {
		return fixedNodeColors;
	}

	public PcpNodeData(int depth, int fixedNodeColors) {
		super();
		this.depth = depth;
		this.fixedNodeColors = fixedNodeColors;
	}
	
}
