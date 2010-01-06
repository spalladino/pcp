package pcp.tests.porta.processing;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pcp.porta.model.Constraint;
import pcp.porta.model.Family;
import pcp.porta.model.Model;
import pcp.porta.processing.Cardinals;
import pcp.porta.processing.Processor;


public class ProcessorFixture {
	
//	@Before
//	public void setup() {
//		
//	}
//	
//	@Test
//	public void shouldNotGroupColorsDifferentNode() {
//		Cardinals c = new Cardinals(5,4,true);
//		Model model = new Model(c);
//		
//		Constraint c1 = model.createConstraint(-1, 2);
//		Constraint c2 = model.createConstraint(-1, 2);
//		
//		c1.addVar(0, 0, -1).addVar(2, 2, 1).addVar(2, 3, 1).addVar(3, 3, 1).addVar(4, 1, 1).addVar(4, 3, 1);
//		c2.addVar(0, 0, -1).addVar(2, 2, 1).addVar(2, 3, 1).addVar(3, 2, 1).addVar(4, 1, 1).addVar(4, 2, 1);
//		
//		Processor proc = new Processor(c);
//		proc.groupConstraintsByColor(model);
//		
//		List<Family> families = model.getFamilies();
//		Assert.assertEquals(0, families.size());
//	}
//	
//	@Test
//	public void shouldNotGroupColorsDifferentNode2() {
//		Cardinals c = new Cardinals(5,4,true);
//		Model model = new Model(c);
//		
//		Constraint c1 = model.createConstraint(-1, 0);
//		Constraint c2 = model.createConstraint(-1, 0);
//		
//		c1.addVar(0, 1, -1);
//		c2.addVar(1, 3, -1);
//		
//		Processor proc = new Processor(c);
//		proc.groupConstraintsByColor(model);
//		
//		List<Family> families = model.getFamilies();
//		Assert.assertEquals(0, families.size());
//	}
//	
//	@Test
//	public void shouldGroupColors() {
//		Cardinals c = new Cardinals(4,3,true);
//		Model model = new Model(c);
//		
//		Constraint c1 = model.createConstraint(0, 0);
//		Constraint c2 = model.createConstraint(0, 0);
//		Constraint c3 = model.createConstraint(0, 0);
//		
//		c1.addVar(2, 0, 4);
//		c2.addVar(2, 1, 4);
//		c3.addVar(2, 2, 4);
//		
//		Processor proc = new Processor(c);
//		proc.groupConstraintsByColor(model);
//		
//		List<Family> families = model.getFamilies();
//		Assert.assertEquals(1, families.size());
//		
//		Family f = families.get(0);
//		Assert.assertEquals(true, f.isColorVariable());
//		Assert.assertEquals(false, f.isNodeVariable());
//		
//		Assert.assertEquals(3, f.getColorValues().size());
//		
//		Assert.assertEquals(4, f.x(2, 0));
//	}
//	
//	@Test
//	public void shouldRestoreFirstColor() {
//		Constraint c = new Constraint(5, 3, 0, 0);
//
//		c.addVar(0, 1, 2);
//		c.addVar(1, 1, 1).addVar(1, 2, 1);
//		c.addVar(2, 1, 3).addVar(2, 2, 3);
//		c.addVar(3, 1, 4).addVar(3, 2, 2);
//		c.addVar(4, 1, -1).addVar(4, 2, -3);
//		
//		restoreFirstColor(c);
//		
//		Assert.assertEquals(0, c.x(0, 0));
//		Assert.assertEquals(2, c.x(0, 1));
//		Assert.assertEquals(0, c.x(0, 2));
//		
//		Assert.assertEquals(-1, c.x(1, 0));
//		Assert.assertEquals(0, c.x(1, 1));
//		Assert.assertEquals(0, c.x(1, 2));
//		
//		Assert.assertEquals(-3, c.x(2, 0));
//		Assert.assertEquals(0, c.x(2, 1));
//		Assert.assertEquals(0, c.x(2, 2));
//		
//		Assert.assertEquals(-2, c.x(3, 0));
//		Assert.assertEquals(2, c.x(3, 1));
//		Assert.assertEquals(0, c.x(3, 2));
//		
//		Assert.assertEquals(1, c.x(4, 0));
//		Assert.assertEquals(0, c.x(4, 1));
//		Assert.assertEquals(-2, c.x(4, 2));
//		
//		Assert.assertEquals(-5, c.getBound());
//	}
//
//	private Constraint restoreFirstColor(Constraint c) {
//		Processor proc = new Processor();
//		proc.restoreNodeFirstColorVariable(c);
//		return c;
//	}	
	
}
