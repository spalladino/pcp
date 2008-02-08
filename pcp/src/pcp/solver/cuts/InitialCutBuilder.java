package pcp.solver.cuts;

import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.List;

import pcp.entities.Node;
import pcp.entities.Partition;
import pcp.interfaces.ICutBuilder;
import pcp.model.Model;

public class InitialCutBuilder implements ICutBuilder {

	public InitialCutBuilder(IloCplex cplex, Model model) {
		this.cplex = cplex;
		this.modeler = cplex;
		this.model = model;
	}
	
	IloCplex cplex;
	IloMPModeler modeler;
	Model model;

	int blockCount = 0;
	
	@Override
	public void addBlockColor(Partition partition, int color) {
		try {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("BLOCK[%1$d]", color);
			
			for (int j = color + 1; j < model.getColorCount(); j++) {
				for (Node node : partition.getNodes()) {
					expr.addTerm(model.x(node.index(), j), 1);
				}  
			}
			
			expr.addTerm(model.w(color), -1);
			
			IloRange range = modeler.le(expr, 0, name);
			cplex.addCut(range);
			blockCount++;
			System.out.println("Got " + blockCount + " block initial cuts");
		} catch (Exception ex) {
			System.err.println("Could not generate block color cut: " + ex.getMessage());
		}	
	}

	@Override
	public void addClique(List<Node> nodes, int color) {
		throw new RuntimeException("Unimplemented");
		
	}

	@Override
	public void addHole(List<Node> nodes, int color) {
		throw new RuntimeException("Unimplemented");
	}
	
}
