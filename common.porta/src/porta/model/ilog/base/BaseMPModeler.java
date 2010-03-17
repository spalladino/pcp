package porta.model.ilog.base;

import java.util.Iterator;

import ilog.concert.IloAddable;
import ilog.concert.IloAnd;
import ilog.concert.IloColumn;
import ilog.concert.IloColumnArray;
import ilog.concert.IloConstraint;
import ilog.concert.IloConversion;
import ilog.concert.IloCopyManager;
import ilog.concert.IloCopyable;
import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloLPMatrix;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloModel;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloObjectiveSense;
import ilog.concert.IloOr;
import ilog.concert.IloRange;
import ilog.concert.IloSOS1;
import ilog.concert.IloSOS2;
import ilog.concert.IloSemiContVar;
import ilog.concert.IloCopyManager.Check;


public class BaseMPModeler implements IloMPModeler {
	
	@Override
	public IloLPMatrix LPMatrix() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLPMatrix LPMatrix(String arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS1 SOS1(IloNumVar[] arg0, double[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS1 SOS1(IloNumVar[] arg0, double[] arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS1 SOS1(IloNumVar[] arg0, double[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS1 SOS1(IloNumVar[] arg0, double[] arg1, int arg2, int arg3, String arg4) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS2 SOS2(IloNumVar[] arg0, double[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS2 SOS2(IloNumVar[] arg0, double[] arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS2 SOS2(IloNumVar[] arg0, double[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS2 SOS2(IloNumVar[] arg0, double[] arg1, int arg2, int arg3, String arg4) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr abs(IloNumExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLPMatrix addLPMatrix() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLPMatrix addLPMatrix(String arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addMaximize() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addMaximize(String arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addMinimize() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addMinimize(String arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addObjective(IloObjectiveSense arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addObjective(IloObjectiveSense arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addRange(double arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addRange(double arg0, double arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS1 addSOS1(IloNumVar[] arg0, double[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS1 addSOS1(IloNumVar[] arg0, double[] arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS1 addSOS1(IloNumVar[] arg0, double[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS1 addSOS1(IloNumVar[] arg0, double[] arg1, int arg2, int arg3, String arg4) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS2 addSOS2(IloNumVar[] arg0, double[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS2 addSOS2(IloNumVar[] arg0, double[] arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS2 addSOS2(IloNumVar[] arg0, double[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSOS2 addSOS2(IloNumVar[] arg0, double[] arg1, int arg2, int arg3, String arg4) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addToExpr(IloObjective arg0, IloNumExpr arg1) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void addToExpr(IloRange arg0, IloNumExpr arg1) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public IloIntVar boolVar(IloColumn arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar boolVar(IloColumn arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] boolVarArray(IloColumnArray arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] boolVarArray(IloColumnArray arg0, String[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumn column(IloLPMatrix arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumn column(IloRange arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumn column(IloObjective arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumn column(IloLPMatrix arg0, int[] arg1, double[] arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumn column(IloLPMatrix arg0, int[] arg1, double[] arg2, int arg3, int arg4) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumnArray columnArray(IloRange arg0, double[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumnArray columnArray(IloObjective arg0, double[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumnArray columnArray(IloLPMatrix arg0, int arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumnArray columnArray(IloRange arg0, double[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumnArray columnArray(IloObjective arg0, double[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloColumnArray columnArray(IloLPMatrix arg0, int arg1, int[][] arg2, double[][] arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConversion conversion(IloNumVar arg0, IloNumVarType arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConversion conversion(IloNumVar[] arg0, IloNumVarType arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConversion conversion(IloNumVar[] arg0, IloNumVarType[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConversion conversion(IloNumVar arg0, IloNumVarType arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConversion conversion(IloNumVar[] arg0, IloNumVarType arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConversion conversion(IloNumVar[] arg0, IloNumVarType[] arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public void delete(IloCopyable arg0) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void delete(IloCopyable[] arg0) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void delete(IloCopyable[] arg0, int arg1, int arg2) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public IloModel getModel() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar intVar(IloColumn arg0, int arg1, int arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar intVar(IloColumn arg0, int arg1, int arg2, String arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] intVarArray(IloColumnArray arg0, int arg1, int arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] intVarArray(IloColumnArray arg0, int[] arg1, int[] arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] intVarArray(IloColumnArray arg0, int arg1, int arg2, String[] arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] intVarArray(IloColumnArray arg0, int[] arg1, int[] arg2, String[] arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective maximize() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective maximize(String arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective minimize() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective minimize(String arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar numVar(IloColumn arg0, double arg1, double arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar numVar(IloColumn arg0, double arg1, double arg2, IloNumVarType arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar numVar(IloColumn arg0, double arg1, double arg2, String arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar numVar(IloColumn arg0, double arg1, double arg2, IloNumVarType arg3, String arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(IloColumnArray arg0, double arg1, double arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(IloColumnArray arg0, double[] arg1, double[] arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(IloColumnArray arg0, double arg1, double arg2, IloNumVarType arg3)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(IloColumnArray arg0, double[] arg1, double[] arg2, IloNumVarType[] arg3)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(IloColumnArray arg0, double arg1, double arg2, String[] arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(IloColumnArray arg0, double[] arg1, double[] arg2, String[] arg3)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(IloColumnArray arg0, double arg1, double arg2, IloNumVarType arg3, String[] arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(IloColumnArray arg0, double[] arg1, double[] arg2, IloNumVarType[] arg3,
			String[] arg4) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective objective(IloObjectiveSense arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective objective(IloObjectiveSense arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr piecewiseLinear(IloNumExpr arg0, double[] arg1, double[] arg2, double arg3, double arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr piecewiseLinear(IloNumExpr arg0, double[] arg1, int arg2, int arg3, double[] arg4, int arg5,
			double arg6, double arg7) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange range(double arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange range(double arg0, double arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar semiContVar(double arg0, double arg1, IloNumVarType arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar semiContVar(double arg0, double arg1, IloNumVarType arg2, String arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar semiContVar(IloColumn arg0, double arg1, double arg2, IloNumVarType arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar semiContVar(IloColumn arg0, double arg1, double arg2, IloNumVarType arg3, String arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar[] semiContVarArray(int arg0, double arg1, double arg2, IloNumVarType arg3)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar[] semiContVarArray(int arg0, double[] arg1, double[] arg2, IloNumVarType[] arg3)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar[] semiContVarArray(IloColumnArray arg0, double arg1, double arg2, IloNumVarType arg3)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar[] semiContVarArray(IloColumnArray arg0, double[] arg1, double[] arg2, IloNumVarType[] arg3)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar[] semiContVarArray(int arg0, double arg1, double arg2, IloNumVarType arg3, String[] arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar[] semiContVarArray(int arg0, double[] arg1, double[] arg2, IloNumVarType[] arg3, String[] arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar[] semiContVarArray(IloColumnArray arg0, double arg1, double arg2, IloNumVarType arg3,
			String[] arg4) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloSemiContVar[] semiContVarArray(IloColumnArray arg0, double[] arg1, double[] arg2, IloNumVarType[] arg3,
			String[] arg4) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setLinearCoef(IloObjective arg0, double arg1, IloNumVar arg2) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoef(IloObjective arg0, IloNumVar arg1, double arg2) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoef(IloRange arg0, double arg1, IloNumVar arg2) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoef(IloRange arg0, IloNumVar arg1, double arg2) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoefs(IloObjective arg0, double[] arg1, IloNumVar[] arg2) throws IloException {
		throw new UnsupportedOperationException();
			
	}
	
	@Override
	public void setLinearCoefs(IloObjective arg0, IloNumVar[] arg1, double[] arg2) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoefs(IloRange arg0, double[] arg1, IloNumVar[] arg2) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoefs(IloRange arg0, IloNumVar[] arg1, double[] arg2) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoefs(IloObjective arg0, double[] arg1, IloNumVar[] arg2, int arg3, int arg4)
			throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoefs(IloObjective arg0, IloNumVar[] arg1, double[] arg2, int arg3, int arg4)
			throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoefs(IloRange arg0, double[] arg1, IloNumVar[] arg2, int arg3, int arg4) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setLinearCoefs(IloRange arg0, IloNumVar[] arg1, double[] arg2, int arg3, int arg4) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setModel(IloModel arg0) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public IloIntExpr abs(IloIntExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addEq(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint addEq(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addEq(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addEq(IloNumExpr arg0, double arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint addEq(IloNumExpr arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addEq(double arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addGe(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint addGe(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addGe(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addGe(IloNumExpr arg0, double arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint addGe(IloNumExpr arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addGe(double arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addLe(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint addLe(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addLe(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addLe(IloNumExpr arg0, double arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint addLe(IloNumExpr arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addLe(double arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addMaximize(IloNumExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addMaximize(IloNumExpr arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addMinimize(IloNumExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addMinimize(IloNumExpr arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addObjective(IloObjectiveSense arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective addObjective(IloObjectiveSense arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addRange(double arg0, IloNumExpr arg1, double arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange addRange(double arg0, IloNumExpr arg1, double arg2, String arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAnd and() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAnd and(IloConstraint[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAnd and(IloConstraint[] arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAnd and(IloConstraint arg0, IloConstraint arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAnd and(IloConstraint[] arg0, int arg1, int arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAnd and(IloConstraint arg0, IloConstraint arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAnd and(IloConstraint[] arg0, int arg1, int arg2, String arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar boolVar() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar boolVar(String arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] boolVarArray(int arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] boolVarArray(int arg0, String[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr constant(double arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr constant(int arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr diff(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr diff(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr diff(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr diff(IloIntExpr arg0, int arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr diff(IloIntExpr arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr diff(int arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint eq(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange eq(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange eq(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint eq(IloNumExpr arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange eq(IloNumExpr arg0, double arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange eq(double arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange ge(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint ge(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange ge(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange ge(IloNumExpr arg0, double arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint ge(IloNumExpr arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange ge(double arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint ifThen(IloConstraint arg0, IloConstraint arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint ifThen(IloConstraint arg0, IloConstraint arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr intExpr() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar intVar(int arg0, int arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar intVar(int arg0, int arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] intVarArray(int arg0, int arg1, int arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] intVarArray(int arg0, int[] arg1, int[] arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] intVarArray(int arg0, int arg1, int arg2, String[] arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntVar[] intVarArray(int arg0, int[] arg1, int[] arg2, String[] arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange le(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint le(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange le(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange le(IloNumExpr arg0, double arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint le(IloNumExpr arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange le(double arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearIntExpr linearIntExpr() throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearIntExpr linearIntExpr(int arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearNumExpr linearNumExpr() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearNumExpr linearNumExpr(double arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr max(IloNumExpr[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr max(IloIntExpr[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr max(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr max(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr max(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr max(IloIntExpr arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr max(int arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr max(IloIntExpr arg0, int arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective maximize(IloNumExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective maximize(IloNumExpr arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr min(IloNumExpr[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr min(IloIntExpr[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr min(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr min(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr min(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr min(IloIntExpr arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr min(int arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr min(IloIntExpr arg0, int arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective minimize(IloNumExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective minimize(IloNumExpr arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr negative(IloNumExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr negative(IloIntExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint not(IloConstraint arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloConstraint not(IloConstraint arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr numExpr() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar numVar(double arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar numVar(double arg0, double arg1, IloNumVarType arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar numVar(double arg0, double arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar numVar(double arg0, double arg1, IloNumVarType arg2, String arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(int arg0, double arg1, double arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(int arg0, double[] arg1, double[] arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(int arg0, double arg1, double arg2, IloNumVarType arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(int arg0, double[] arg1, double[] arg2, IloNumVarType[] arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(int arg0, double arg1, double arg2, String[] arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(int arg0, double[] arg1, double[] arg2, String[] arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(int arg0, double arg1, double arg2, IloNumVarType arg3, String[] arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumVar[] numVarArray(int arg0, double[] arg1, double[] arg2, IloNumVarType[] arg3, String[] arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective objective(IloObjectiveSense arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloObjective objective(IloObjectiveSense arg0, IloNumExpr arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloOr or() throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloOr or(IloConstraint[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloOr or(IloConstraint[] arg0, String arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloOr or(IloConstraint arg0, IloConstraint arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloOr or(IloConstraint[] arg0, int arg1, int arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloOr or(IloConstraint arg0, IloConstraint arg1, String arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloOr or(IloConstraint[] arg0, int arg1, int arg2, String arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr prod(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr prod(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr prod(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr prod(IloIntExpr arg0, int arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr prod(IloIntExpr arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr prod(int arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr prod(double arg0, IloNumVar arg1, IloNumVar arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr prod(IloNumVar arg0, double arg1, IloNumVar arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr prod(IloNumVar arg0, IloNumVar arg1, double arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange range(double arg0, IloNumExpr arg1, double arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloRange range(double arg0, IloNumExpr arg1, double arg2, String arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearNumExpr scalProd(IloNumVar[] arg0, double[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearNumExpr scalProd(double[] arg0, IloNumVar[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearNumExpr scalProd(IloNumVar[] arg0, int[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearNumExpr scalProd(int[] arg0, IloNumVar[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr scalProd(IloNumVar[] arg0, IloNumVar[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearIntExpr scalProd(int[] arg0, IloIntVar[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearIntExpr scalProd(IloIntVar[] arg0, int[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr scalProd(IloIntVar[] arg0, IloIntVar[] arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearNumExpr scalProd(IloNumVar[] arg0, double[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearNumExpr scalProd(double[] arg0, IloNumVar[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr scalProd(IloNumVar[] arg0, IloNumVar[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearIntExpr scalProd(int[] arg0, IloIntVar[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearIntExpr scalProd(IloIntVar[] arg0, int[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr scalProd(IloIntVar[] arg0, IloIntVar[] arg1, int arg2, int arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr square(IloNumExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr square(IloIntExpr arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr arg0, double arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(double arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr arg0, IloNumExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr arg0, int arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(int arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr arg0, IloIntExpr arg1) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr arg0, IloNumExpr arg1, IloNumExpr arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr[] arg0, int arg1, int arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr arg0, IloIntExpr arg1, IloIntExpr arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr[] arg0, int arg1, int arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr arg0, IloNumExpr arg1, IloNumExpr arg2, IloNumExpr arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr arg0, IloIntExpr arg1, IloIntExpr arg2, IloIntExpr arg3) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr arg0, IloNumExpr arg1, IloNumExpr arg2, IloNumExpr arg3, IloNumExpr arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr arg0, IloIntExpr arg1, IloIntExpr arg2, IloIntExpr arg3, IloIntExpr arg4)
			throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr arg0, IloNumExpr arg1, IloNumExpr arg2, IloNumExpr arg3, IloNumExpr arg4,
			IloNumExpr arg5) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr arg0, IloIntExpr arg1, IloIntExpr arg2, IloIntExpr arg3, IloIntExpr arg4,
			IloIntExpr arg5) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr arg0, IloNumExpr arg1, IloNumExpr arg2, IloNumExpr arg3, IloNumExpr arg4,
			IloNumExpr arg5, IloNumExpr arg6) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr arg0, IloIntExpr arg1, IloIntExpr arg2, IloIntExpr arg3, IloIntExpr arg4,
			IloIntExpr arg5, IloIntExpr arg6) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloNumExpr sum(IloNumExpr arg0, IloNumExpr arg1, IloNumExpr arg2, IloNumExpr arg3, IloNumExpr arg4,
			IloNumExpr arg5, IloNumExpr arg6, IloNumExpr arg7) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloIntExpr sum(IloIntExpr arg0, IloIntExpr arg1, IloIntExpr arg2, IloIntExpr arg3, IloIntExpr arg4,
			IloIntExpr arg5, IloIntExpr arg6, IloIntExpr arg7) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAddable add(IloAddable arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAddable[] add(IloAddable[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAddable[] add(IloAddable[] arg0, int arg1, int arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterator iterator() {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAddable remove(IloAddable arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAddable[] remove(IloAddable[] arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloAddable[] remove(IloAddable[] arg0, int arg1, int arg2) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getName() {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setName(String arg0) {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public IloCopyable makeCopy(IloCopyManager arg0) throws IloException {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public void needCopy(Check arg0) throws Check {
		throw new UnsupportedOperationException();
		
	}
	
}
