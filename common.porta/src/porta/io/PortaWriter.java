package porta.io;

import java.io.PrintStream;

import porta.interfaces.ITermWriter;
import porta.model.BaseConstraint;
import porta.model.Model;
import porta.processing.ITranslator;
import exceptions.AlgorithmException;


public abstract class PortaWriter {
	
	protected Model<?, ?, ?> model;
	protected Integer dim;
	protected ITranslator translator;
	
	public PortaWriter(Model<?, ?, ?> model, ITranslator translator) {
		this.model = model;
		this.dim = calculateDimension();
		this.translator = translator;
	}
	
	protected abstract Integer calculateDimension();

	public void write(String filename) throws Exception {
		try {
			PrintStream stream = new PrintStream(filename);
			innerWriteIeq(stream);
			stream.close();
		} catch (Exception ex) {
			System.err.println("Error trying to print ieq file to " + filename);
			System.err.println(ex.getMessage());
			throw ex;
		}
	}

	void innerWriteIeq(PrintStream stream) throws AlgorithmException {
		writeDimension(stream);
		writeLowerBounds(stream);
		writeUpperBounds(stream);
		writeValidPoint(stream);
		writeInequalities(stream);
		writeEnd(stream);
	}

	void writeEnd(PrintStream stream) {
		stream.print("\nEND\n");
	}

	void writeDimension(PrintStream stream) {
		stream.print("DIM = " + this.dim + "\n");
	}

	void writeLowerBounds(PrintStream stream) {
		stream.print("\nLOWER_BOUNDS\n");
		for (int i = 0; i < this.dim; i++) {
			stream.print(0);
			stream.print(' ');
		} stream.print('\n');
	}
	
	void writeUpperBounds(PrintStream stream) {
		stream.print("\nUPPER_BOUNDS\n");
		for (int i = 0; i < this.dim; i++) {
			stream.print(1);
			stream.print(' ');
		} stream.print('\n');
	}
	
	protected abstract void writeValidPoint(PrintStream stream) throws AlgorithmException;

	protected abstract boolean skipConstraint(BaseConstraint constraint);
	
	void writeInequalities(PrintStream stream) {
		stream.print("\nINEQUALITIES_SECTION\n");
		boolean inEqs = true;
		
		for (Object obj : model.getConstraints()) {
			BaseConstraint constraint = (BaseConstraint)obj;
			if (skipConstraint(constraint)) {
				continue;
			}
			
			if (constraint.getCompare() != 0 && inEqs) {
				inEqs = false;
				stream.print('\n');
			}
			
			stream.print(constraint.toString(createTermWriter()));
			stream.print('\n');
		}
	}
	
	protected abstract ITermWriter createTermWriter();
	
}
