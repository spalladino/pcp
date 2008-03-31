package pcp.utils;

import java.util.List;

import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.interfaces.IModelData;


public class DataUtils {
	
	public static double sumXi(List<Node> nodes, int color, IModelData data) {
		double sum = 0.0;
		for (Node node : nodes) {
			sum += data.x(node.index(), color);
		} return sum;
	}
	
	public static double sumPartsXi(List<Partition> partitions, int color, IModelData data) {
		double sum = 0.0;
		for (Partition p : partitions) {
			for (Node n : p.getNodes()) {
				sum += data.x(n.index(), color);
			}
		} return sum;
	}
	
}
