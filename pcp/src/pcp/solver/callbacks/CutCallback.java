package pcp.solver.callbacks;

import java.util.List;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import pcp.entities.Node;
import pcp.interfaces.ICutBuilder;


public class CutCallback extends IloCplex.CutCallback implements ICutBuilder {

	@Override
	protected void main() throws IloException {
		
		
		
	}

	@Override
	public void addClique(List<Node> nodes, int color) {
		
		
	}
	
}
