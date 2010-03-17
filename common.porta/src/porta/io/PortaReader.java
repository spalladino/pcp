package porta.io;

import java.io.FileInputStream;
import java.io.InputStream;

import porta.interfaces.IFactory;
import porta.model.Model;
import porta.parser.PortaParser;
import porta.processing.ITranslator;

public class PortaReader {
	
	Model<?, ?, ?> model;
	IFactory factory;
	
	public PortaReader(Model<?, ?, ?> m, IFactory factory) {
		this.model = m;
		this.factory = factory;
	}
	
	public void read(String filename) throws Exception {
		read(new FileInputStream(filename));
	}
	
	public void read(InputStream s) throws Exception {
		ITranslator t = factory.createTranslator();
		PortaParser parser = new PortaParser(s);
		parser.initialize(this.model, t);
		parser.ieq();
	}
	
}
