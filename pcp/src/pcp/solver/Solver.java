package pcp.solver;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.CplexStatus;
import pcp.Settings;
import pcp.interfaces.IPartitionedGraph;
import pcp.model.Model;
import pcp.solver.cuts.InitialCutBuilder;
import pcp.solver.cuts.InitialCutsGenerator;
import pcp.solver.data.AbstractSolutionData;


public class Solver extends AbstractSolutionData {
	
	static final boolean useInitialSolution = Settings.get().getBoolean("start.useInitialSolution"); 
	
	IloCplex cplex;
	Model model;
	
	boolean solved;
	double epsilon = 1.0e-6;
	long elapsed = 0L;
	
	public Solver() throws IloException {
		this.cplex = new IloCplex();
	}
	
	public boolean solve() throws IloException {
		System.out.println("Solving with " + this.getClass().getName());
		if (model.isTrivial()) return true;
		long start = System.currentTimeMillis();
		beforeSolve();
		solved = this.cplex.solve();
		long end = System.currentTimeMillis();
		elapsed = end - start;
		
		return solved;
	}
	
	protected void beforeSolve() {
		if(useInitialSolution) {
			loadInitialSolution();
		}
	}

	protected void loadInitialSolution() {
		// TODO: Setvectors
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
	public double[] xs(int node) {
		try {
			return cplex.getValues(model.getXs()[node]);
		} catch(Exception ex) {return null;}
	}
	
	@Override
	public double[] ws()  {
		try {
			return cplex.getValues(model.getWs());
		} catch(Exception ex) {return null;}
	}
	
	@Override
	public IPartitionedGraph getGraph() {
		return model.getGraph();
	}

	@Override
	public double w(int j)  {
		try {
			return cplex.getValue(model.w(j));
		} catch(Exception ex) {return 0.0;}
	}

	@Override
	public double x(int i, int j)  {
		try {
			return cplex.getValue(model.x(i,j));
		} catch(Exception ex) {return 0.0;}
	}
	
	protected void addInitialCuts() {
		InitialCutsGenerator generator = new InitialCutsGenerator(model);
		InitialCutBuilder builder = new InitialCutBuilder(cplex, model);
		generator.makeCuts(builder);
	}

}
