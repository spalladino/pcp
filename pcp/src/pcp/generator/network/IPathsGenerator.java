package pcp.generator.network;

import java.util.List;

public interface IPathsGenerator {

	List<Lightpath> generate(RequestsMatrix requests);
	
}
