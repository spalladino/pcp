package pcp.solver;

import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.BooleanParam;
import ilog.cplex.IloCplex.CplexStatus;
import ilog.cplex.IloCplex.DoubleParam;
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

/**
 * Base class for all solvers.
 */
public class Solver extends AbstractSolutionData {
	
	final static boolean useHeuristicCallback = Settings.get().getBoolean("solver.useHeuristicCallback");
	final static boolean useBranchingCallback = Settings.get().getBoolean("solver.useBranchingCallback");
	final static boolean useCplexPrimal = Settings.get().getBoolean("solver.useCplexPrimalHeuristic");
	
	final static double maxTime = Settings.get().getDouble("solver.maxTime");
	
	IloCplex cplex;
	Model model;
	
	HeuristicCallback heurCallback;
	BranchCallback branchCallback;
	
	boolean solved;
	double elapsed = 0.0;
	
	public Solver() throws IloException {
		this.cplex = new IloCplex();
		cplex.setParam(IntParam.Threads, 1);
		
		if (maxTime > 0.0) { 
			cplex.setParam(DoubleParam.TiLim, maxTime);
		}
		
		if (!useCplexPrimal) {
			turnOffCplexPrimalHeur();
		}
	}
	
	public boolean solve() throws Exception {
		System.out.println("Solving with " + this.getClass().getName());
		if (model.isTrivial()) return true;
		
		// Presolve, solve it and measure time
		double start = cplex.getCplexTime();
		beforeSolve();
		solved = this.cplex.solve();
		double end = cplex.getCplexTime();
		elapsed = end - start;

		afterSolve();
		return solved;
	}

	protected void beforeSolve() throws IloException, AlgorithmException { }
	
	protected void afterSolve() throws IloException, AlgorithmException { 
		if (heurCallback != null) {
			heurCallback.getMetrics().printTotal();
		}
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
				vals[idx] = coloring.getIntColor(i) == j ? 1 : 0;
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
	
	public double getGap() throws IloException {
		return cplex.getMIPRelativeGap();
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
		data.put("solution.chi", solved ? getChromaticNumber() : 0);
		data.put("solution.lb", solved ? cplex.getBestObjValue() : 0);
		data.put("solution.success", solved);
		data.put("solution.gap", solved ? this.getGap() : 0);
		data.put("solution.nnodes", cplex.getNnodes());
		data.put("solution.time", elapsed);
		data.put("solution.solver", this.getClass().getName());
		
		
		
		if (heurCallback != null) {
			heurCallback.getMetrics().fillData(data);
		}
	}

	protected void setBranchingSettings() throws IloException {
		if (useBranchingCallback) {
			System.out.println("Using custom branch callback");	
			cplex.use(this.branchCallback = new BranchCallback(model));
		}
		
		if (useHeuristicCallback) {
			System.out.println("Using custom heuristic callback");
			cplex.use(this.heurCallback = new HeuristicCallback(model));
		}
		
		new BranchingPrioritizer(cplex, model).setPriorities();
		new BranchingDirectioner(cplex, model).setDirection();
		new BranchingSelector(cplex, model).setSelection();
	}

	protected void addInitialCuts() {
		InitialCutsGenerator generator = new InitialCutsGenerator(model);
		InitialCutBuilder builder = new InitialCutBuilder(cplex, model);
		generator.makeCuts(builder);
	}
	
	protected void turnOffCplexPrimalHeur() throws IloException {
		cplex.setParam(IntParam.HeurFreq, -1);
	}
	
	
	protected void turnOffCplexCuts() throws IloException {
		cplex.setParam(BooleanParam.PreLinear, false);
        
        cplex.setParam(IntParam.Cliques, -1);
        cplex.setParam(IntParam.Covers, -1);
        cplex.setParam(IntParam.DisjCuts, -1);
        cplex.setParam(IntParam.FlowCovers, -1);
        cplex.setParam(IntParam.FlowPaths, -1);
        cplex.setParam(IntParam.FracCuts, -1);
        cplex.setParam(IntParam.GUBCovers, -1);
        cplex.setParam(IntParam.ImplBd, -1);
        cplex.setParam(IntParam.ZeroHalfCuts, -1);
        cplex.setParam(IntParam.MIRCuts, -1);
        
	}
	
	protected void turnOffPreprocess() throws IloException {
		cplex.setParam(IntParam.AggInd, 0);
		cplex.setParam(IntParam.BndStrenInd, 0);
		cplex.setParam(IntParam.CoeRedInd, 0);
		cplex.setParam(BooleanParam.PreInd, false);
        cplex.setParam(IntParam.PrePass, 0);
        cplex.setParam(IntParam.RelaxPreInd, 0);
		cplex.setParam(IntParam.Reduce, 0);
        cplex.setParam(IntParam.RepeatPresolve, 0);
        cplex.setParam(IntParam.PreDual, -1);
	}
	
	protected void turnOffDynamicSearch() throws IloException {
		cplex.setParam(IntParam.MIPSearch, 1);
	}

}
