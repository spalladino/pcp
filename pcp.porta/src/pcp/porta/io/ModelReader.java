package pcp.porta.io;

import java.io.FileInputStream;
import java.io.InputStream;

import pcp.porta.model.Model;
import pcp.porta.parser.ModelParser;


public class ModelReader {

	public ModelReader() {
	}
	
	public Model read(String filename) throws Exception {
		return read(new FileInputStream(filename));
	}
	
	public Model read(InputStream s) throws Exception {
		ModelParser parser = new ModelParser(s);
		return parser.model();
	}
}
