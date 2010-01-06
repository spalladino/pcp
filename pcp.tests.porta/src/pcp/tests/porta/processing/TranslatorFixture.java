package pcp.tests.porta.processing;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pcp.Settings;
import pcp.common.TupleInt;
import pcp.porta.processing.Cardinals;
import pcp.porta.processing.Translator;


public class TranslatorFixture {
	
	int colors = 3;
	int nodes = 4;
	
	Set<PortaNodeColor> pnc;
	Cardinals p;
	Translator t;
	
	@Before
	public void setup() {
		Settings.init();
		pnc = new HashSet<PortaNodeColor>();
		p = new Cardinals(nodes, colors);
		t = new Translator(p);
	}
	
	@Test
	public void shouldConvertFromPortaToNodeColor() {
		for(int i = 0; i < colors * nodes + colors; i++) {
			TupleInt nodeColor = t.convertPortaToNodeColor(i+1);
			pnc.add(new PortaNodeColor(i+1, nodeColor.getFirst(), nodeColor.getSecond()));
		} checkVars(p);
	}
	
	@Test
	public void shouldConvertFromNodeColorToPorta() {
		for(int i = 0; i <  nodes; i++) {
			for(int j = 0; j < colors; j++) {
				int porta = t.convertNodeColorToPorta(i, j);
				pnc.add(new PortaNodeColor(porta, i, j));
			}
		}
		for(int j = 0; j < colors; j++) {
			int porta = t.convertNodeColorToPorta(null, j);
			pnc.add(new PortaNodeColor(porta, null, j));
		}

		checkVars(p);
	}

	private void checkVars(Cardinals p) {
		checkVar("x[1,1]", 1, 0, 0);
		checkVar("x[1,2]", 2, 0, 1);
		checkVar("x[1,3]", 3, 0, 2);
		checkVar("x[2,1]", 4, 1, 0);
		checkVar("x[2,2]", 5, 1, 1);
		checkVar("x[2,3]", 6, 1, 2);
		checkVar("x[3,1]", 7, 2, 0);
		checkVar("x[3,2]", 8, 2, 1);
		checkVar("x[3,3]", 9, 2, 2);
		checkVar("x[4,1]", 10, 3, 0);
		checkVar("x[4,2]", 11, 3, 1);
		checkVar("x[4,3]", 12, 3, 2);
		
		checkVar("w[1]", 13, null, 0);
		checkVar("w[2]", 14, null, 1);
		checkVar("w[3]", 15, null, 2);
	}

	private void checkVar(String name, Integer index, Integer node, Integer color) {
		Assert.assertTrue(pnc.contains(new PortaNodeColor(index, node, color)));
	}

	private static class PortaNodeColor {
		Integer porta;
		
		Integer node;
		Integer color;
		
		public PortaNodeColor(Integer porta, Integer node, Integer color) {
			this.porta = porta;
			this.node = node;
			this.color = color;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((color == null) ? 0 : color.hashCode());
			result = prime * result + ((node == null) ? 0 : node.hashCode());
			result = prime * result + ((porta == null) ? 0 : porta.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			PortaNodeColor other = (PortaNodeColor) obj;
			if (color == null) {
				if (other.color != null) return false;
			} else if (!color.equals(other.color)) return false;
			if (node == null) {
				if (other.node != null) return false;
			} else if (!node.equals(other.node)) return false;
			if (porta == null) {
				if (other.porta != null) return false;
			} else if (!porta.equals(other.porta)) return false;
			return true;
		}
		
		public Integer getPorta() {
			return porta;
		}
		
		public Integer getNode() {
			return node;
		}
		
		public Integer getColor() {
			return color;
		}
		
		
	}
	
}
