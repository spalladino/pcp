package pcp.porta.processing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pcp.porta.Parameters;
import pcp.porta.model.Variable;
import porta.base.BaseParameters;
import porta.processing.ITranslator;


public class Translator implements ITranslator<Variable> {
	
	Parameters p;
	
	static Pattern xRegex = Pattern.compile("(X|x)\\[(\\d+),(\\d+)\\]");
	static Pattern wRegex = Pattern.compile("(W|w)\\[(\\d+)\\]");
	
	public Translator(Parameters cardinals) {
		this.p = cardinals;
	}
	
	@Override
	public Variable convertNameToModel(String name) {
		Matcher xmatcher = xRegex.matcher(name);
		Matcher wmatcher = wRegex.matcher(name);
		Integer node = null, color = null;
		
		if (xmatcher.matches()) {
			node = Integer.valueOf(xmatcher.group(2));
			color = Integer.valueOf(xmatcher.group(3));
		} else if (wmatcher.matches()) {
			color = Integer.valueOf(wmatcher.group(2));
		} else {
			throw new RuntimeException("Could not parse name " + name);
		}
		
		return new Variable(node, color);
	}
	
	@Override
	public Variable convertPortaToModel(int index) {
		Integer color = null;
		Integer node = null;
		--index;
		
		if (index >= p.nodeVarsCount) {
			color = index - p.nodeVarsCount;
		} else {
			node = index / p.colorCount;
			color = index % p.colorCount;
		}
		
		return new Variable(node, color);
	}
	
	@Override
	public int convertModelToPorta(Variable var) {
		if (var.getNode() == null) {
			return var.getColor() + p.nodeVarsCount + 1; 
		} else {
			return var.getNode() * p.colorCount + var.getColor()+ 1;
		}
	}

	@Override
	public BaseParameters getParameters() {
		return p;
	}
}
