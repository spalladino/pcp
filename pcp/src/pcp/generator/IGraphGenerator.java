package pcp.generator;

import java.io.Writer;

public interface IGraphGenerator {

	void generate(Writer writer, String name) throws Exception;

}