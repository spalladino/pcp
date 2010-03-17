package pcp.porta.io;

import java.io.PrintStream;

import pcp.porta.model.Constraint;
import pcp.porta.model.Model;
import pcp.porta.model.Variable;
import pcp.porta.processing.Translator;
import pcp.utils.NameUtils;
import porta.interfaces.ITermWriter;
import porta.model.BaseConstraint;
import porta.model.BaseVariable;
import props.Settings;
import exceptions.AlgorithmException;

public class PortaWriter extends porta.io.PortaWriter<Variable> {
	
	boolean validPoint;
	boolean projectColors;
	
	int colorVars = 0;
	int nodeVars = 0;
	
	int nodeCount = 0;
	int colorCount = 0;
	
	public PortaWriter(Model model) {
		super(model, new Translator(model.getCardinals()));

		this.validPoint = Settings.get().getBoolean("writer.validPoint");
		this.projectColors = Settings.get().getBoolean("porta.projectColors");

		this.colorVars = model.getCardinals().colorCount;
		this.nodeVars = model.getCardinals().nodeVarsCount;
		this.nodeCount = model.getCardinals().nodeCount;
		this.colorCount = model.getCardinals().colorCount;;
	}
	
	@Override
	protected Integer calculateDimension() {
		int count = projectColors ? 0 : colorVars;
		return nodeVars + count;
	}

	
	protected porta.interfaces.ITermWriter createTermWriter() {
		return new ITermWriter() {
			public String term(int coef, BaseVariable v) {
				Variable var = (Variable) v;
				if (var.getNode() == null && projectColors) {
					return NameUtils.asTerm(coef);
				} else {
					int porta = translator.convertModelToPorta(var);
					return NameUtils.asCoef(coef) + "x" + String.valueOf(porta);
				}
			}
		};
	}


	@Override
	protected boolean skipConstraint(BaseConstraint constraint) {
		return projectColors && ((Constraint)constraint).isOnlyColors();
	}

	@Override
	protected void writeValidPoint(PrintStream stream) throws AlgorithmException {
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
	
}
