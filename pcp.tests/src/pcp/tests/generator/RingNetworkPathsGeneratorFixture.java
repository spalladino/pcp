package pcp.tests.generator;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pcp.generator.network.Lightpath.Route;
import pcp.generator.network.Lightpath;
import pcp.generator.network.RequestsMatrix;
import pcp.generator.network.RingNetworkPathsGenerator;


public class RingNetworkPathsGeneratorFixture {

	@Test
	public void shouldGenerateBothPathsForSingleRequestFromLowerToHigher() {
		RequestsMatrix matrix = new RequestsMatrix(6).withRequest(2, 4);
		RingNetworkPathsGenerator generator = new RingNetworkPathsGenerator(6);
		
		List<Lightpath> pathslist = generator.generate(matrix);		
		
		Assert.assertEquals(1, pathslist.size());
		Lightpath path = pathslist.get(0);
		Assert.assertEquals(2, path.getRoutes().size());
		
		Assert.assertEquals(new Route().withNodes(2,3,4), path.getRoutes().get(0));
		Assert.assertEquals(new Route().withNodes(2,1,0,5,4), path.getRoutes().get(1));
	}
	
	@Test
	public void shouldGenerateBothPathsForSingleRequestFromHigherToLower() {
		RequestsMatrix matrix = new RequestsMatrix(6).withRequest(4, 2);
		RingNetworkPathsGenerator generator = new RingNetworkPathsGenerator(6);
		
		List<Lightpath> pathslist = generator.generate(matrix);		
		
		Assert.assertEquals(1, pathslist.size());
		Lightpath path = pathslist.get(0);
		Assert.assertEquals(2, path.getRoutes().size());
		
		Assert.assertEquals(new Route().withNodes(4,5,0,1,2), path.getRoutes().get(0));
		Assert.assertEquals(new Route().withNodes(4,3,2), path.getRoutes().get(1));
		
	}
}
