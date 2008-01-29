package pcp.utils;

import java.util.List;

import pcp.entities.Node;
import pcp.interfaces.IModelData;

public class DataUtils {

	public static double sumXi(List<Node> nodes, int color, IModelData data) {
		double sum = 0.0;
		for (Node node : nodes) {
			sum += data.x(node.index(), color);
		} return sum;
	}

}
