package pcp.algorithms.clique;

import java.util.Comparator;
import java.util.List;

import pcp.Settings;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.SortedPartitionedGraph;
import pcp.interfaces.IAlgorithmSource;


public class ExtendedCliqueCutter extends ExtendedCliqueDetector {

	static double maxColorValue = 1.0;
	static double minColorValue = Epsilon;
	static double minInitialNodeValue = Settings.get().getDouble("clique.minInitialNodeValue");
	static double minCandidateNodeValue = Settings.get().getDouble("clique.minCandidateNodeValue");
	
	double valueWj;
	double valueSumXij;

	public ExtendedCliqueCutter(IAlgorithmSource provider) {
		super(provider);
	}

	@Override
	protected List<Integer> getColors(boolean asc) {
		return provider.getSorted().getSortedColors(asc);
	}

	@Override
	protected boolean onStartColor() {
		this.valueWj = data.w(color);
		if (valueWj < minColorValue) return false;
		if (valueWj > maxColorValue) return false;
		return true;
	}

	@Override
	protected SortedPartitionedGraph generateGraph() {
		return provider.getSorted().getSortedGraph(color, Desc);
	}

	@Override
	protected boolean onInitialNode(Node initial) {
		if ((data.x(initial.index(), color) < minInitialNodeValue)) return false;
		valueSumXij = data.x(initial.index(), color);
		return true;
	}

	@Override
	protected void onAddedCandidate(Node y) {
		valueSumXij += data.x(y.index(), color);
	}

	@Override
	protected void onRemovedCandidate(Node y) {
		valueSumXij -= data.x(y.index(), color);
	}

	@Override
	protected boolean isCliqueExploitable() {
		return valueSumXij > valueWj + Epsilon;
	}

	@Override
	protected Comparator<Node> getNodeComparator() {
		return provider.getSorted().getNodeComparator(color, Desc);
	}
	
	@Override
	protected boolean isInvalidInitialCandidate(Node node) {
		return data.x(node.index(), color) < minCandidateNodeValue;
	}

}
