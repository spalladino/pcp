package pcp.porta.io;

import java.io.FileInputStream;
import java.io.InputStream;

import pcp.porta.model.Model;
import pcp.porta.parser.PortaParser;
import pcp.porta.processing.Translator;

public class PortaReader {
	
	Model model;
	
	public PortaReader(Model m) {
		this.model = m;
	}
	
	public void read(String filename) throws Exception {
		read(new FileInputStream(filename));
	}
	
	public void read(InputStream s) throws Exception {
		Translator t = new Translator(model.getCardinals());
		PortaParser parser = new PortaParser(s);
		parser.initialize(this.model, t);
		parser.ieq();
	}
	
}
