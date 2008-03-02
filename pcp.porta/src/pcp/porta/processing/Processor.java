package pcp.porta.processing;

import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.model.BuilderStrategy;
import pcp.model.strategy.Partition;
import pcp.porta.model.Constraint;
import pcp.porta.model.Model;
import pcp.utils.IntUtils;

public class Processor {

	final Cardinals cards;
	final BuilderStrategy strategy;
	final IPartitionedGraph graph;
	
	public Processor(IPartitionedGraph g, Cardinals c, BuilderStrategy strategy) {
		this.graph = g;
		this.cards = c;
		this.strategy = strategy;
	}

	public void process(Model model) {
		if (strategy.getPartitionConstraints() == Partition.PaintExactlyOne) {
			restoreNodeFirstColorVariable(model);
		} groupConstraintsByColor(model);
	}
	
	/**
	 * Attempts to group similar constraints with different colors
	 */
	public void groupConstraintsByColor(Model model) {
		new ConstraintsMatcher(model){
			public boolean process(Constraint c) {
				current.setColorVariable();
				current.getColorValues().add(0);
				return true;
			}
			public boolean match(Constraint c1, Constraint c2) {
				Integer diff = colorsDifference(c1, c2);
				if (diff != null) {
					current.getColorValues().add(diff);
					current.representingConstraint(c2);
					return true;
				} else {
					return false;
				}
			}
		}.run();
	}

	/**
	 * Whenever an expression like x[i,1] + ... + x[i,c-1] is found,
	 * it is replaced by x[i,0]. 
	 */
	public void restoreNodeFirstColorVariable(Model model) {
		for (Constraint c : model.getConstraints()) {
			restoreNodeFirstColorVariable(c);
		}
	}
	
	/**
	 * Whenever an expression like x[i,1] + ... + x[i,c-1] is found,
	 * it is replaced by x[i,0], considering all nodes in partition. 
	 */
	public void restoreNodeFirstColorVariable(Constraint constraint) {
		for (pcp.entities.partitioned.Partition partition : graph.getPartitions()) {
			Node[] nodes = partition.getNodes();
			
			int i = 0;
			Integer minMult = null;
			while ((minMult == null || minMult != 0) && i < nodes.length) {
				minMult = hasEveryNodeColor(constraint, nodes[i].index(), true, minMult); 
				i++;
			}
			
			if (minMult != 0) {
				for (i = 0; i < nodes.length; i++) {
					for (int j = 0; j < constraint.getColorCount(); j++) {
						constraint.addX(nodes[i].index(), j, -minMult);
					}
				}
				constraint.addBound(-minMult);
			}
		}
	}

	private class ConstraintInfo {
		public Integer minColor = null; 
		public Integer maxColor = null;
		public Integer minNodeIndex = null;
		public Integer maxNodeIndex = null;
		public Integer minNodeColor = null;
		public Integer maxNodeColor = null;
		public int nodeTermCount;
		public int colorTermCount;
	}
	
	@SuppressWarnings("unused")
	private boolean inBounds(int i, int j) {
		return i >= 0 && i < cards.nodeCount && j >= 0 && j < cards.colorCount;
	}
	
	@SuppressWarnings("unused")
	private boolean colorInBounds(int j) {
		return j >= 0 && j < cards.colorCount;
	}
	
	/**
	 * Given an expression and a node, returns the minimum multiplicity (in abs)
	 * of all (optionally non-first) color variables for that node. If there are coefficients 
	 * with different sign, then zero is returned.  
	 */
	private int hasEveryNodeColor(Constraint expr, int i, boolean nonFirst, Integer current) {
		int initial = nonFirst ? 1 : 0;
		for (int j = initial; j < expr.getColorCount(); j++) {
			int coef = expr.x(i, j);
			
			if (coef == 0) {
				return 0;
			} else if (current == null) { 
				current = coef;
			} else if (current * coef < 0) {
				return 0;
			} else {
				current = IntUtils.minabs(current, coef);
			}
		} 
		return current;
	}
	
	private ConstraintInfo constraintInfo(Constraint c) {
		ConstraintInfo info = new ConstraintInfo();
		@SuppressWarnings("unused")
		int colorCoef, nodeCoef;
		for (int j = 0; j < c.getColorCount(); j++) {
			if ((colorCoef = c.w(j)) != 0) {
				info.colorTermCount++; 
				info.minColor = IntUtils.min(info.minColor, j);
				info.maxColor = IntUtils.max(info.maxColor, j); 
			}
			for (int i = 0; i < c.getNodeCount(); i++) {
				if ((nodeCoef = c.x(i, j)) != 0) {
					info.nodeTermCount++;
					info.minNodeColor = IntUtils.min(info.minNodeColor, j);
					info.maxNodeColor = IntUtils.max(info.maxNodeColor, j);
					info.minNodeIndex = IntUtils.min(info.minNodeIndex, i);
					info.maxNodeIndex = IntUtils.max(info.maxNodeIndex, i);
				}
			}
		} return info;
	}

	/**
	 * Returns the difference between colors for similar terms between two constraints,
	 * or null if there is no exact difference.
	 */
	private Integer colorsDifference(Constraint c1, Constraint c2) {
		ConstraintInfo info1 = constraintInfo(c1);
		ConstraintInfo info2 = constraintInfo(c2);
		
		if (c1.getBound() != c2.getBound() 
			|| c1.getCompare() != c2.getCompare()) 
			return null;
		
		if (info1.colorTermCount != info2.colorTermCount 
			|| info1.nodeTermCount != info2.nodeTermCount) return null;
		
		Integer diff = null;
		if(info1.colorTermCount != 0) {
			diff = info2.maxColor - info1.maxColor;
			if (diff != info2.minColor - info1.minColor) return null;
		}
		if(info1.nodeTermCount != 0) {
			Integer diff2;
			diff2 = info2.maxNodeColor - info1.maxNodeColor;
			if (diff2 != info2.minNodeColor - info1.minNodeColor) {
				return null;
			}
			if (diff != null && diff != diff2) {
				return null;
			} else {
				diff = diff2;
			}
		}
		
		if (diff == null || diff == 0) 
			return null;
		
		if (info1.colorTermCount > 0) {
			for (int j = info1.minColor; j < info1.maxColor; j++) {
				if (c1.w(j) != 0 && c1.w(j) != c2.w(j+diff)) {
					return null;
				} 
			}
		}
		
		if (info1.nodeTermCount > 0) {
			for (int j = info1.minNodeColor; j <= info1.maxNodeColor; j++) {
				for (int i = 0; i < c1.getNodeCount(); i++) {
					if (c1.x(i, j) != 0 && c1.x(i, j) != c2.x(i, j+diff)) {
						return null;
					}
				}
			}
		}
		
		return diff;
	}
}
