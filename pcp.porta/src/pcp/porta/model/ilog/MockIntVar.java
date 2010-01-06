package pcp.porta.model.ilog;

import pcp.porta.model.ilog.base.BaseIntVar;


public class MockIntVar extends BaseIntVar {

	String name;

	public MockIntVar(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
