package pcp.porta.io;

import java.io.PrintStream;

import pcp.Settings;
import pcp.algorithms.AlgorithmException;
import pcp.porta.model.Constraint;
import pcp.porta.model.Model;
import pcp.porta.model.Constraint.ITermWriter;
import pcp.porta.processing.Translator;
import pcp.utils.NameUtils;


public class PortaWriter {
	
	Model model;
	Integer dim;
	Translator translator;
	
	boolean validPoint;
	boolean projectColors;
	
	int colorVars = 0;
	int nodeVars = 0;
	
	int nodeCount = 0;
	int colorCount = 0;
	
	public PortaWriter(Model model) {
		this.validPoint = Settings.get().getBoolean("writer.validPoint");
		this.projectColors = Settings.get().getBoolean("porta.projectColors");

		this.colorVars = model.getCardinals().colorCount;
		this.nodeVars = model.getCardinals().nodeVarsCount;
		this.nodeCount = model.getCardinals().nodeCount;
		this.colorCount = model.getCardinals().colorCount;;
		
		this.model = model;
		this.dim = calculateDimension();
		this.translator = new Translator(model.getCardinals());
		
	}
	
	private Integer calculateDimension() {
		int count = projectColors ? 0 : colorVars;
		return nodeVars + count;
	}

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
	
	void writeValidPoint(PrintStream stream) throws AlgorithmException {
		if (!this.validPoint) return;
		
		if (colorCount != nodeCount) {
			throw new AlgorithmException("Node count and color count must be equal to write valid point.");
		}
		
		stream.print("\nVALID\n");
		
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < colorCount; j++) {
				if (j == i) {
					stream.print(1);
				} else {
					stream.print(0);
				}
				stream.print(' ');
			}
		}
		
		if (!projectColors) {
			for (int i = nodeVars; i < nodeVars + colorVars; i++) {
				stream.print(1);
				stream.print(' ');
			} 
		}

		stream.print('\n');
	}

	void writeInequalities(PrintStream stream) {
		stream.print("\nINEQUALITIES_SECTION\n");
		boolean inEqs = true;
		
		for (Constraint constraint : model.getConstraints()) {
			if (projectColors && constraint.isOnlyColors()) {
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
	
	ITermWriter createTermWriter() {
		return new ITermWriter() {
			public String term(int coef, Integer i, Integer j) {
				if (i == null && projectColors) {
					return NameUtils.asTerm(coef);
				} else {
					int porta = translator.convertNodeColorToPorta(i, j);
					return NameUtils.asCoef(coef) + "x" + String.valueOf(porta);
				}
			}
		};
	}
	
}
