package pcp.algorithms.holes;

import java.util.List;

public interface IHolesDetector<N>{
	
	public static interface IHoleHandler<NODE> {
    	boolean handle(List<NODE> hole);
    }
    
    public static interface IHoleFilter<NODE> {
    	boolean use(List<NODE> hole);
    }

    void holes(IHoleHandler<N> handler, IHoleFilter<N> filter);
}