package pcp.solver.cuts;


public enum CutFamily {
	
	BlockColor,
	Hole,
	Path,
	Clique,
	GPHole,
	GPPath;

	public static int count() {
		return CutFamily.values().length;
	}
	
	public static String getName(int index) {
		return CutFamily.values()[index].toString();
	}
	
}
