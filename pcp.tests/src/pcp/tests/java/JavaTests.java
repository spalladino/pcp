package pcp.tests.java;

import junit.framework.Assert;

import org.junit.Test;


public class JavaTests {

	@Test
	public void testStringFormat() {
		Assert.assertEquals("x[42,93]", 
				String.format("x[%1$d,%2$d]", 42, 93));
	}
	
}
