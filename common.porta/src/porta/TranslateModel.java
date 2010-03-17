package porta;

import porta.interfaces.IFactory;
import porta.io.PortaWriter;
import porta.model.Model;


public class TranslateModel {

	IFactory factory;
	
	public TranslateModel(IFactory factory) {
		this.factory = factory;
	}
	
	public void translate(String specificmodelfile, String specificportafile) throws Exception {
		
		Model<?, ?, ?> model = factory.readModel(specificportafile);
		
		System.out.println("Parsed input from " + specificmodelfile);
		
		PortaWriter writer = factory.createPortaWriter(model);
		writer.write(specificportafile);
		
		System.out.println("Generated output in " + specificportafile);
	}

	
	
}
