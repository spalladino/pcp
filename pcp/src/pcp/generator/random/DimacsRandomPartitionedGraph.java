package pcp.generator.random;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

import pcp.generator.DimacsPartitionedGraph;
import pcp.generator.GraphProperties;

class DimacsRandomPartitionedGraph extends DimacsPartitionedGraph {

	int minPartition, maxPartition;
	double edgeProb;
	Random random;
	
	public DimacsRandomPartitionedGraph(GraphProperties props) {
		super(props);
		this.maxPartition = props.getMaxPartition();
		this.minPartition = props.getMinPartition();
		this.edgeProb = props.getEdgeProb();
		
		this.random = new Random(System.currentTimeMillis());
		this.edgeList = new ArrayList<Edge>();
		this.partitionsList = new ArrayList<Partition>();
		
		// Check edge probability
		if (edgeProb < 0) edgeProb = 0;
		else if (edgeProb > 1) edgeProb = 1;
		
		// Check partition sizes
		if (minPartition < 1) minPartition = 1;
		if (maxPartition < minPartition) maxPartition = minPartition;
		
		// Create axes
		for (int i = 0; i < nodes; i++)
			for (int j = i+1; j < nodes; j++)
				if (edgeProb > random.nextDouble())
					this.edgeList.add(new Edge(i,j));

		// Create partitions
		int nodeIndex = 0;
		while (nodeIndex < nodes) {
			int currSize = random.nextInt(maxPartition - minPartition + 1) + minPartition;
			Partition partition = new Partition();
			partitionsList.add(partition);
			while (currSize > 0 && nodeIndex < nodes) { 
				partition.addNode(nodeIndex);
				nodeIndex++;
				currSize--;
			}
		}
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write("c density=" + String.valueOf(edgeProb) + "\n");
		super.write(writer);
	}
	
	

	
}
