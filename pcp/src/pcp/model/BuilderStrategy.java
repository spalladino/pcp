package pcp.model;

import pcp.model.strategy.Adjacency;
import pcp.model.strategy.Coloring;
import pcp.model.strategy.Partition;
import pcp.model.strategy.Symmetry;
import props.Settings;



public class BuilderStrategy  {
	
	Adjacency adjacency;
	Partition partition;
	Symmetry breakSymmetry;
	Coloring coloring;
	
	public BuilderStrategy() {
		
	}
	
	public BuilderStrategy(Partition partition,
			Adjacency adjacency,
			Symmetry breakSymmetry,
			Coloring coloring) {
		this.adjacency = adjacency;
		this.partition = partition;
		this.breakSymmetry = breakSymmetry;
		this.coloring = coloring;
	}

	public Symmetry getBreakSymmetry() {
		return breakSymmetry;
	}
	
	public Adjacency getAdjacencyConstraints() {
		return adjacency;
	}
	
	public Coloring getColoring() {
		return coloring;
	}
	
	public Partition getPartitionConstraints() {
		return partition;
	}

	public static BuilderStrategy createDefault() {
		return new BuilderStrategy(Partition.PaintExactlyOne, Adjacency.AdjacentsLeqColor, Symmetry.UseLowerLabelFirst, Coloring.Partitions);
	}
	
	public static BuilderStrategy fromSettings() {
		Settings s = Settings.get();
		BuilderStrategy strategy = new BuilderStrategy();
		strategy.coloring = s.getEnum("strategy.coloring", Coloring.class);
		strategy.partition = s.getEnum("strategy.partition", Partition.class);
		strategy.breakSymmetry = s.getEnum("strategy.symmetry", Symmetry.class);
		strategy.adjacency = s.getEnum("strategy.adjacency", Adjacency.class);
		return strategy;
	}

	public String shortId() {
		return new StringBuilder()
			.append(coloring.getShortId()).append(".")
			.append(partition.getShortId()).append(".")
			.append(breakSymmetry.getShortId()).append(".")
			.append(adjacency.getShortId())
			.toString();
	}
	
}
