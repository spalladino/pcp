package pcp.model;

import pcp.model.strategy.Adjacency;
import pcp.model.strategy.ColorBound;
import pcp.model.strategy.Coloring;
import pcp.model.strategy.Objective;
import pcp.model.strategy.Partition;
import pcp.model.strategy.Symmetry;
import props.Settings;



public class BuilderStrategy  {
	
	Adjacency adjacency;
	Partition partition;
	Symmetry breakSymmetry;
	Coloring coloring;
	Objective objective;
	ColorBound colorBound;
	
	private static BuilderStrategy settings;
	
	public BuilderStrategy() {
		
	}
	
	public BuilderStrategy(Partition partition,
			Adjacency adjacency,
			Symmetry breakSymmetry,
			Coloring coloring,
			Objective objective,
			ColorBound colorBound) {
		this.adjacency = adjacency;
		this.partition = partition;
		this.breakSymmetry = breakSymmetry;
		this.coloring = coloring;
		this.objective = objective;
		this.colorBound = colorBound;
	}

	public ColorBound getColorBound() {
		return colorBound;
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
	
	public Objective getObjective() {
		return objective;
	}

	public static BuilderStrategy createDefault() {
		return new BuilderStrategy(Partition.PaintExactlyOne, Adjacency.AdjacentsLeqColor, Symmetry.UseLowerLabelFirst, Coloring.Partitions, Objective.Equal, ColorBound.None);
	}
	
	public static BuilderStrategy fromSettings() {
		if (settings == null) {
			Settings s = Settings.get();
			BuilderStrategy strategy = new BuilderStrategy();
			strategy.coloring = s.getEnum("strategy.coloring", Coloring.class);
			strategy.partition = s.getEnum("strategy.partition", Partition.class);
			strategy.breakSymmetry = s.getEnum("strategy.symmetry", Symmetry.class);
			strategy.adjacency = s.getEnum("strategy.adjacency", Adjacency.class);
			strategy.objective = s.getEnum("strategy.objective", Objective.class);
			strategy.colorBound = s.getEnum("strategy.colorBound", ColorBound.class);
			settings = strategy;
		} return settings;
	}

	public String shortId() {
		return new StringBuilder()
			.append(coloring.getShortId()).append(".")
			.append(partition.getShortId()).append(".")
			.append(breakSymmetry.getShortId()).append(".")
			.append(adjacency.getShortId()).append(".")
			.append(colorBound.getShortId())
			.toString();
	}
	
}
