package pcp.model.parsing;

import java.io.BufferedReader;
import java.io.IOException;

import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.interfaces.IGraphParser;

public class DimacsParser implements IGraphParser {
	
	private int base = 1;
	
	public PartitionedGraphBuilder parse(BufferedReader reader) throws DimacsParseException, IOException {
		String[] tokens = readProblemLine(reader);
		if (tokens == null) return null;
		
		int nodeCount = Integer.valueOf(tokens[2]);
		int edgeCount = Integer.valueOf(tokens[3]);
		int partCount = Integer.valueOf(tokens[4]);
	
		PartitionedGraphBuilder builder = new PartitionedGraphBuilder(tokens[1]);
		
		readPartitionsList(reader, builder, partCount, nodeCount);
		readEdgesList(reader, builder, edgeCount, nodeCount);
		
		checkCounts(nodeCount, edgeCount, partCount, builder);
		
		return builder;
	}

	private void checkCounts(int nodeCount, int edgeCount, int partCount, PartitionedGraphBuilder builder) throws DimacsParseException {
		if (nodeCount != builder.getNodes().length) {
			throw new DimacsParseException(String.format("Node count does not match: expected %1$d but found %2$d", nodeCount, builder.getNodes().length));
		}
		
		if (partCount != builder.getPartitions().length) {
			throw new DimacsParseException(String.format("Partition count does not match: expected %1$d but found %2$d", partCount, builder.getPartitions().length));
		}
		
		if (edgeCount != builder.getEdges().length) {
			throw new DimacsParseException(String.format("Edge count does not match: expected %1$d but found %2$d", edgeCount, builder.getEdges().length));
		}
	}
	
	private void readPartitionsList(BufferedReader reader, PartitionedGraphBuilder builder, int partitionsCount, int nodeCount)
			throws DimacsParseException, IOException {
		String line;
		int partition = 0;
		
		while (partition < partitionsCount) {
			line = reader.readLine();
			if (line == null) throw new DimacsParseException("Missing partitions in graph");
			if (line.startsWith("c")) continue;
			
			if (line.startsWith("q")) {
				String[] tokens = line.trim().split(" ");
				boolean first = true;
				for (String token : tokens) {
					if (!first) {
						int node = Integer.valueOf(token) - base;
						checkNode(node, nodeCount);
						builder.addNode(node, partition);
					} else {
						first = false;
					}
				}
			} else {
				throw new DimacsParseException("Missing partitions in graph, expected q, found " + line);
			}
			partition++;
		}
	}
	
	private void checkNode(int node, int nodeCount) throws DimacsParseException {
		if (node < 0 || node >= nodeCount) {
			throw new DimacsParseException("Node " + node + " out of range.");
		}
	}

	private void readEdgesList(BufferedReader reader, PartitionedGraphBuilder builder, int edgeCount, int nodeCount)
			throws DimacsParseException, IOException {
		String line;
		
		while (edgeCount > 0) {
			line = reader.readLine();
			if (line == null) throw new DimacsParseException("Missing edges in graph");
			if (line.startsWith("c")) continue;
			
			if (line.startsWith("e")) {
				String[] tokens = line.trim().split(" ");
				if (tokens.length != 3 || !tokens[0].equalsIgnoreCase("e")) {
					throw new DimacsParseException("Invalid edge line format " + line);
				}
				int n1 = Integer.valueOf(tokens[1]) - base;
				int n2 = Integer.valueOf(tokens[2]) - base;
				checkNode(n1, nodeCount);
				checkNode(n2, nodeCount);
				builder.addEdge(n1, n2);
			} else {
				throw new DimacsParseException("Missing edges in graph, expected e, found " + line);
			}	
			edgeCount--;
		}
	}
	
	private static String[] readProblemLine(BufferedReader reader) throws IOException, DimacsParseException {
		String problemLine;
		
		do {
			problemLine = reader.readLine();
		} while (problemLine != null && problemLine.charAt(0) == 'c');
		
		if (problemLine == null) return null;
		
		String[] tokens = problemLine.split(" ");
		if (tokens.length !=5 || !tokens[0].equalsIgnoreCase("p")) {
			throw new DimacsParseException("Expecting problem line with parameters: name nodecount edgecount partitioncount");
		}
		
		return tokens;
	}
	
	public int getBase() {
		return base;
	}
	
	public void setBase(int base) {
		this.base = base;
	}
}
