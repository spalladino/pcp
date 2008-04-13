package pcp.solver;

import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.CplexStatus;
import ilog.cplex.IloCplex.IntParam;

import java.util.Map;

import pcp.definitions.Constants;
import pcp.entities.IPartitionedGraph;
import pcp.model.Model;
import pcp.solver.branching.BranchingDirectioner;
import pcp.solver.branching.BranchingPrioritizer;
import pcp.solver.branching.BranchingSelector;
import pcp.solver.callbacks.BranchCallback;
import pcp.solver.callbacks.HeuristicCallback;
import pcp.solver.cuts.InitialCutBuilder;
import pcp.solver.cuts.InitialCutsGenerator;
import pcp.solver.data.AbstractSolutionData;
import props.Settings;
import exceptions.AlgorithmException;


public class Solver extends AbstractSolutionData {
	
	final static boolean useHeuristicCallback = Settings.get().getBoolean("solver.useHeuristicCallback");
	final static boolean useBranchingCallback = Settings.get().getBoolean("solver.useBranchingCallback");
	
	IloCplex cplex;
	Model model;
	
	boolean solved;
	double elapsed = 0.0;
	
	public Solver() throws IloException {
		this.cplex = new IloCplex();
	}
	
	public boolean solve() throws Exception {
		System.out.println("Solving with " + this.getClass().getName());
		if (model.isTrivial()) return true;
		double start = cplex.getCplexTime();
		setBranchingSettings();
		beforeSolve();
		solved = this.cplex.solve();
		double end = cplex.getCplexTime();
		elapsed = end - start;
		
		return solved;
	}
	
	public void loadInitialSolution(pcp.algorithms.coloring.ColoringAlgorithm coloring) throws IloException, AlgorithmException {
		cplex.setParam(IntParam.AdvInd, 1);
		int n = model.getNodeCount();
		int c = model.getColorCount();
		int chi = coloring.getChi();
		
		double[] vals = new double[n*c + c];
		IloNumVar[] vars = new IloNumVar[n*c + c];
		
		int idx = 0;
		for (int j = 0; j < c; j++) {
			// Set w variable value
			vars[idx] = model.getWs()[j];
			vals[idx] = j < chi ? 1 : 0; 
			idx++;
			for (int i = 0; i < n; i++) {
				// Set x variable value
				vars[idx] = model.getXs()[i][j];
				vals[idx] = coloring.getColor(i) == j ? 1 : 0;
				idx++;
			}
		}
		
		// Set the initial solution
		cplex.setVectors(vals, null, vars, null, null, null);
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
	
	public int getChromaticNumber() throws IloException {
		if (model.isTrivial()) return 1;
		return (int)Math.ceil(cplex.getObjValue() - Constants.Epsilon);
	}

	public CplexStatus getStatus() throws IloException {
		return cplex.getCplexStatus();
	}
	
	public double getTime() {
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
	
	public void fillExecutionData(Map<String,Object> data) throws Exception {
		data.put("solution.chi", getChromaticNumber());
		data.put("solution.time", elapsed);
		data.put("solution.solver", this.getClass().getName());
	}

	protected void beforeSolve() throws IloException, AlgorithmException {
	}

	protected void setBranchingSettings() throws IloException {
		if (useHeuristicCallback) cplex.use(new HeuristicCallback(model));
		if (useBranchingCallback) cplex.use(new BranchCallback(model));
		
		new BranchingPrioritizer(cplex, model).setPriorities();
		new BranchingDirectioner(cplex, model).setDirection();
		new BranchingSelector(cplex, model).setSelection();
	}

	protected void addInitialCuts() {
		InitialCutsGenerator generator = new InitialCutsGenerator(model);
		InitialCutBuilder builder = new InitialCutBuilder(cplex, model);
		generator.makeCuts(builder);
	}

}
