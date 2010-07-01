package pcp.tests.utils;

import org.junit.Test;

import junit.framework.Assert;
import pcp.utils.RandomUtils;

public class RandomUtilsFixture {

	@Test
	public void testRandoms() {
		for (int i = 0; i < 100; i++) {
			int j = RandomUtils.pickGeometrical(5);
			Assert.assertTrue(j >= 0);
			Assert.assertTrue(j <  5);
			System.out.println(j);
		}
	}
	
}
