package pcp.solver;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.CplexStatus;
import ilog.cplex.IloCplex.UnknownObjectException;
import pcp.interfaces.IPartitionedGraph;
import pcp.model.Model;


public class Solver extends AbstractSolutionData  {
	
	IloCplex cplex;
	Model model;
	
	boolean solved;
	double epsilon = 1.0e-6;
	long elapsed = 0L;
	
	public Solver() throws IloException {
		this.cplex = new IloCplex();
	}
	
	public boolean solve() throws IloException {
		if (model.isTrivial()) return true;
		long start = System.currentTimeMillis();
		solved = this.cplex.solve();
		long end = System.currentTimeMillis();
		elapsed = end - start;
		
		return solved;
	}
	
	public void end() throws Exception {
		cplex.end();
	}

	public IloCplex getCplex() {
		return cplex;
	}

	public Model getModel() {
		return model;
	}
	
	public void setModel(Model model) {
		this.model = model; 
	}
	
	public boolean isSolved() {
		return solved;
	}
	
	public int getChi() throws IloException {
		if (model.isTrivial()) return 1;
		return (int)Math.ceil(cplex.getObjValue() - epsilon);
	}

	public CplexStatus getStatus() throws IloException {
		return cplex.getCplexStatus();
	}
	
	public long getTime() {
		return elapsed;
	}

	@Override
	public double[] getNodeValues(int node) throws UnknownObjectException, IloException {
		return cplex.getValues(model.getXs()[node]);
	}
	
	@Override
	public double[] getColorValues() throws UnknownObjectException, IloException {
		return cplex.getValues(model.getWs());
	}
	
	@Override
	public IPartitionedGraph getGraph() {
		return model.getGraph();
	}
	
}
