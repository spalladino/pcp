package pcp.porta.io;

import java.io.PrintStream;

import pcp.algorithms.AlgorithmException;
import pcp.porta.model.Constraint;
import pcp.porta.model.Family;
import pcp.porta.model.Model;
import pcp.porta.model.Constraint.ITermWriter;


public class ModelWriter extends PortaWriter {

	public ModelWriter(Model model) {
		super(model);
	}
	
	@Override
	void innerWriteIeq(PrintStream stream) throws AlgorithmException {
		writeCardinals(stream);
		writeInequalities(stream);
		writeFamilies(stream);
	}
	
	void writeFamilies(PrintStream stream) {
		stream.print('\n');
		for (Family f : model.getFamilies()) {
			if (!(projectColors && f.isOnlyColors())) {
				stream.print(f.toString());
				stream.print('\n');
			}
		}
	}

	void writeCardinals(PrintStream stream) {
		stream.print("COLORS: " + model.getCardinals().colorCount+ "\n");
		stream.print("NODES:  " + model.getCardinals().nodeCount + "\n");
		stream.print("\n\n");
	}
	
	@Override
	ITermWriter createTermWriter() {
		return Constraint.defaultTermWriter(1);
	}
	
}
