package pcp.porta.io;

import java.io.PrintStream;

import pcp.porta.model.Constraint;
import pcp.porta.model.PcpModel;
import pcp.porta.processing.Translator;
import porta.interfaces.ITermWriter;
import porta.model.BaseConstraint;
import porta.model.BaseFamily;
import exceptions.AlgorithmException;


public class ModelWriter extends porta.io.ModelWriter {

	PortaWriter inner;
	
	public ModelWriter(PcpModel model) {
		super(model, new Translator(model.getCardinals()));
		if (inner == null) inner = new PortaWriter(model);
	}
	
	@Override
	protected void writeCardinals(PrintStream stream) {
		stream.print("COLORS: " + inner.colorCount+ "\n");
		stream.print("NODES:  " + inner.nodeCount + "\n");
		stream.print("\n\n");
	}
	
	public ITermWriter createTermWriter() {
		return Constraint.defaultTermWriter(1);
	}

	@Override
	protected boolean skipFamily(BaseFamily family) {
		return skipConstraint(family);
	}

	@Override
	protected Integer calculateDimension() {
		if (inner == null) inner = new PortaWriter((PcpModel)model);
		return inner.calculateDimension();
	}

	@Override
	protected boolean skipConstraint(BaseConstraint constraint) {
		return inner.skipConstraint(constraint);
	}

	@Override
	protected void writeValidPoint(PrintStream stream) throws AlgorithmException {
		inner.writeValidPoint(stream);
	}
	
}
