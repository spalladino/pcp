package pcp.generator.network;

import java.util.Random;

public class RequestsMatrix {

	int nodeCount;
	boolean[][] requests;
	Random random;
	
	public RequestsMatrix(int nodeCount) {
		this.nodeCount = nodeCount;
		this.requests = new boolean[nodeCount][nodeCount];
		this.random = new Random();
	}
	
	public RequestsMatrix withRequest(int from, int to) {
		this.requests[from][to] = true;
		return this;
	}
	
	public boolean hasRequest(int from, int to) {
		return this.requests[from][to];
	}
	
	public RequestsMatrix populateRandom(double prob) {
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) {
				if (i != j) {
					if (random.nextDouble() < prob) {
						withRequest(i, j);
					}
				}
			}	
		} return this;
	}
	
}
