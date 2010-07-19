package pcp.solver.data;

public class NodeData {
	
	private final int depth;
	
	private final int branchedNode;
	private final int branchedColor;
	private final int branchDirection;
	
	private final boolean branchDataSet;
	
	public NodeData(int depth) {
		this.depth = depth;
		this.branchedNode = -1;
		this.branchedColor = -1;
		this.branchDirection = 0;
		this.branchDataSet = false;
	}
	
	public NodeData(int depth, int branchedNode, int branchedColor,
			int branchDirection) {
		this.depth = depth;
		this.branchedNode = branchedNode;
		this.branchedColor = branchedColor;
		this.branchDirection = branchDirection;
		this.branchDataSet = true;
	}

	public boolean isBranchDataSet() {
		return branchDataSet;
	}

	public int getDepth() {
		return depth;
	}

	public int getBranchedNode() {
		return branchedNode;
	}

	public int getBranchedColor() {
		return branchedColor;
	}

	public int getBranchDirection() {
		return branchDirection;
	}
	
	public NodeData cloneWithDirection(int direction) {
		return new NodeData(this.depth, this.branchedNode, this.branchedColor, direction);
	}
	
	public static int getDepth(Object data) {
		if (data != null && data instanceof NodeData) {
			NodeData ndata = (NodeData) data;
			return ndata.depth;
		} return 0;
	}
	
	public static int getDirection(Object data) {
		if (data != null) {
			NodeData ndata = (NodeData) data;
			return ndata.branchDirection;
		} return 0;
	}
	
	@Override
	public String toString() {
		if (!branchDataSet) {
			return String.format("D%1$d", depth);
		} else {
			String dir = "X";
			if (branchDirection == 1) dir = "U";
			else if (branchDirection == -1) dir = "D";
			return String.format("D%1$d X[%2$d,%3$d] %4$s", depth, branchedNode, branchedColor, dir);
		}
	}
	
	public static String nodeDataToString(Object obj) {
		if (obj == null) return "X";
		else return obj.toString();
	}
}
