package pcp.solver;

import java.util.Map;

import pcp.Logger;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

public class CplexBranchAndCutSolver extends Solver {

	public CplexBranchAndCutSolver() throws IloException {
		super();
		setMipParameters();
		turnOffDynamicSearch();
	}
	
	@Override
	public void fillData(Map<String, Object> data) {
		super.fillData(data);
		
		try {
			data.put("cplex.cuts.gub", cplex.getNcuts(IloCplex.CutType.GUBCover));
			data.put("cplex.cuts.clique", cplex.getNcuts(IloCplex.CutType.CliqueCover));
			data.put("cplex.cuts.cover", cplex.getNcuts(IloCplex.CutType.Cover));
			data.put("cplex.cuts.implied", cplex.getNcuts(IloCplex.CutType.ImplBd));
			data.put("cplex.cuts.zerohalf", cplex.getNcuts(IloCplex.CutType.ZeroHalf));
			data.put("cplex.cuts.total", 
					cplex.getNcuts(IloCplex.CutType.ZeroHalf) +
					cplex.getNcuts(IloCplex.CutType.ImplBd) +
					cplex.getNcuts(IloCplex.CutType.Cover) +
					cplex.getNcuts(IloCplex.CutType.CliqueCover) +
					cplex.getNcuts(IloCplex.CutType.GUBCover));
		} catch (IloException ex) {
			Logger.error("Error filling solver data", ex);
		}
	}

}
