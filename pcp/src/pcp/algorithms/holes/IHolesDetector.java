package pcp.algorithms.holes;

import java.util.List;

import pcp.entities.Node;

public interface IHolesDetector {
	
	public static interface IHoleHandler {
    	boolean handle(List<Node> hole);
    }
    
    public static interface IHoleFilter {
    	boolean use(List<Node> hole);
    }
    
    public static IHoleFilter AllFilter = new IHoleFilter() {
		public boolean use(List<Node> hole) {
			return true;
		}
    };
	
    void holes(IHoleHandler handler, IHoleFilter filter);
}