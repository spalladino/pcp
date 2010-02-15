package pcp.porta;

import pcp.model.BuilderStrategy;
import pcp.porta.io.ModelReader;
import pcp.porta.io.PortaWriter;
import pcp.porta.model.Model;


public class TranslateModel {

	public static void translate(String specificmodelfile, String specificportafile, BuilderStrategy strategy) throws Exception {
		
		ModelReader reader = new ModelReader();
		Model model = reader.read(specificmodelfile);
		
		System.out.println("Parsed input from " + specificmodelfile);
		
		PortaWriter writer = new PortaWriter(model);
		writer.write(specificportafile);
		
		System.out.println("Generated output in " + specificportafile);
	}

	
	
}
