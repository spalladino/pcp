package porta.io;

import java.io.FileInputStream;
import java.io.InputStream;

import porta.model.BaseModel;
import porta.parser.PortaParser;
import porta.processing.ITranslator;

public class PortaReader {
	
	BaseModel<?, ?, ?> model;
	ITranslator<?> translator;
	
	public PortaReader(BaseModel<?, ?, ?> m, ITranslator<?> translator) {
		this.model = m;
		this.translator = translator;
	}
	
	public void read(String filename) throws Exception {
		read(new FileInputStream(filename));
	}
	
	public void read(InputStream s) throws Exception {
		PortaParser parser = new PortaParser(s);
		parser.initialize(this.model, translator);
		parser.ieq();
	}
	
}
