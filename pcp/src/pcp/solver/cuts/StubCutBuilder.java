package pcp.solver.cuts;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import entities.Tuple;

import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.interfaces.ICutBuilder;


public class StubCutBuilder implements ICutBuilder {

	List<Tuple<Integer, List<pcp.entities.simple.Node>>> gprimeHoles = new ArrayList<Tuple<Integer,List<pcp.entities.simple.Node>>>();
	List<Tuple<Integer, List<pcp.entities.simple.Node>>> gprimePaths = new ArrayList<Tuple<Integer,List<pcp.entities.simple.Node>>>();
	List<Tuple<Integer, List<Node>>> cliques = new ArrayList<Tuple<Integer,List<Node>>>();
	List<Tuple<Integer, List<Node>>> holes = new ArrayList<Tuple<Integer,List<Node>>>();
	List<Tuple<Integer, List<Node>>> paths = new ArrayList<Tuple<Integer,List<Node>>>();
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

	@Override
	public void addGPrimeHole(List<pcp.entities.simple.Node> hole, int color) {
		gprimeHoles.add(new Tuple<Integer, List<pcp.entities.simple.Node>>(color, hole));
	}
	
	@Override
	public void addGPrimePath(List<pcp.entities.simple.Node> hole, int color) {
		gprimePaths.add(new Tuple<Integer, List<pcp.entities.simple.Node>>(color, hole));
	}

	@Override
	public void addPath(List<Node> path, int color) {
		paths.add(new Tuple<Integer, List<Node>>(color, path));
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

	public List<Tuple<Integer, List<pcp.entities.simple.Node>>> getGprimeHoles() {
		return gprimeHoles;
	}

	public void printIneqs(PrintStream stream) {
		print("Cliques", stream, cliques);
		print("Holes", stream, holes);
		print("Paths", stream, paths);
		printGPrime("GPrime Holes", stream, gprimeHoles);
		printGPrime("GPrime Paths", stream, gprimePaths);
	}

	private void print(String title, PrintStream stream, List<Tuple<Integer, List<Node>>> ineqs) {
		stream.print(title);
		stream.print("\n");
		for (Tuple<Integer, List<Node>> t : ineqs) {
			stream.print(' ');
			stream.print(t);
			stream.print('\n');
		}
	}
	
	private void printGPrime(String title, PrintStream stream, List<Tuple<Integer, List<pcp.entities.simple.Node>>> ineqs) {
		stream.print(title);
		stream.print("\n");
		for (Tuple<Integer, List<pcp.entities.simple.Node>> t : ineqs) {
			stream.print(' ');
			stream.print(t);
			stream.print('\n');
		}
	}
	
}
