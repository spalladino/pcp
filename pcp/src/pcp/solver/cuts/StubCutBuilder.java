package pcp.solver.cuts;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import pcp.common.Tuple;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.interfaces.ICutBuilder;


public class StubCutBuilder implements ICutBuilder {

	List<Tuple<Integer, List<Node>>> cliques = new ArrayList<Tuple<Integer,List<Node>>>();
	List<Tuple<Integer, List<Node>>> holes = new ArrayList<Tuple<Integer,List<Node>>>();
	List<Tuple<Integer, Partition>> blocks = new ArrayList<Tuple<Integer,Partition>>();
	
	@Override
	public void addClique(List<Node> nodes, int color) {
		cliques.add(new Tuple<Integer, List<Node>>(color, nodes));
	}

	@Override
	public void addHole(List<Node> nodes, int color) {
		holes.add(new Tuple<Integer, List<Node>>(color, nodes));
	}

	@Override
	public void addBlockColor(Partition partition, int j0) {
		blocks.add(new Tuple<Integer, Partition>(j0, partition));		
	}

	public List<Tuple<Integer, List<Node>>> getCliques() {
		return cliques;
	}
	
	public List<Tuple<Integer, Partition>> getBlocks() {
		return blocks;
	}

	public List<Tuple<Integer, List<Node>>> getHoles() {
		return holes;
	}

	public void printIneqs(PrintStream stream) {
		stream.print("Clique:\n");
		for (Tuple<Integer, List<Node>> t : cliques) {
			stream.print(' ');
			stream.print(t);
			stream.print('\n');
		}
		
		stream.print("Hole:\n");
		for (Tuple<Integer, List<Node>> t : holes) {
			stream.print(' ');
			stream.print(t);
			stream.print('\n');
		}
	}
	
}
