package pcp.tests.porta.parsing;

import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pcp.Settings;
import pcp.common.TupleInt;
import pcp.porta.model.Constraint;
import pcp.porta.model.Model;
import pcp.porta.parser.PortaParser;
import pcp.porta.processing.Cardinals;
import pcp.porta.processing.Translator;


public class PortaParseFixture {

	@Before
	public void setup() throws Exception {
		Settings.load("porta.test");
	}
	
	@Test
	public void test() throws Exception{
		String t = " " +
				"DIM = 3\n" +
				"\n" +
				"VALID\n" +
				"1 1 1\n" +
				"\n" +
				"INEQUALITIES_SECTION\n" +
				"( 1) +x1 + x2 + x3 == 0\n" +
				"\n" +
				"( 1) +2x1 - x2 + x3 <= 4\n" +
				"( 2) -x1 + x2 - 4x3 >= -3\n" +
				"\n" +
				"END\n";
		
		Model constraints = parseFromString(t);
		Assert.assertEquals(3, constraints.getConstraints().size());
		
		Constraint c0 = constraints.getConstraints().get(0);
		Constraint c1 = constraints.getConstraints().get(1);
		Constraint c2 = constraints.getConstraints().get(2);
		
		Assert.assertEquals(0, c0.getCompare());
		Assert.assertEquals(-1, c1.getCompare());
		Assert.assertEquals(1, c2.getCompare());
		
		Assert.assertEquals(0, (int)c0.getBound());
		Assert.assertEquals(4, (int)c1.getBound());
		Assert.assertEquals(-3, (int)c2.getBound());
		
		int c0x1 = c0.x(0, 0);
		int c0x2 = c0.x(1, 0);
		int c0x3 = c0.x(2, 0);
		
		int c1x1 = c1.x(0, 0);
		int c1x2 = c1.x(1, 0);
		int c1x3 = c1.x(2, 0);
		
		int c2x1 = c2.x(0, 0);
		int c2x2 = c2.x(1, 0);
		int c2x3 = c2.x(2, 0);
		
		Assert.assertEquals(1, c0x1);
		Assert.assertEquals(1, c0x2);
		Assert.assertEquals(1, c0x3);
		
		Assert.assertEquals(2, c1x1);
		Assert.assertEquals(-1, c1x2);
		Assert.assertEquals(1, c1x3);
		
		Assert.assertEquals(-1, c2x1);
		Assert.assertEquals(1, c2x2);
		Assert.assertEquals(-4, c2x3);
	
	}
	
	private static Model parseFromString(String input) throws Exception {
		final StringReader reader = new StringReader(input);
		Cardinals c = new Cardinals(3, 1); 		
		Model model = new Model(c);
		
		Translator t = new Translator(c) {
			public TupleInt convertPortaToNodeColor(int index) {
				return new TupleInt(index-1, 0);
			}
		};
		
		PortaParser parser = new PortaParser(reader);
		parser.initialize(model, t);
		
		return parser.ieq();
	}
	
}
