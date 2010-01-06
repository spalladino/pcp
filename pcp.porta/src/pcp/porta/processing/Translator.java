package pcp.porta.processing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pcp.common.TupleInt;


public class Translator {
	
	Cardinals p;
	
	static Pattern xRegex = Pattern.compile("(X|x)\\[(\\d+),(\\d+)\\]");
	static Pattern wRegex = Pattern.compile("(W|w)\\[(\\d+)\\]");
	
	public Translator(Cardinals cardinals) {
		this.p = cardinals;
	}
	
	public TupleInt convertNameToNodeColor(String name) {
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
		
		return new TupleInt(node, color);
	}
	
	public TupleInt convertPortaToNodeColor(int index) {
		Integer color = null;
		Integer node = null;
		--index;
		
		if (index >= p.nodeVarsCount) {
			color = index - p.nodeVarsCount;
		} else {
			node = index / p.colorCount;
			color = index % p.colorCount;
		}
		
		return new TupleInt(node, color);
	}
	
	public int convertNodeColorToPorta(Integer node, Integer color) {
		if (node == null) {
			return color + p.nodeVarsCount + 1; 
		} else {
			return node * p.colorCount + color + 1;
		}
	}

	public Cardinals getCardinals() {
		return p;
	}
	
}
