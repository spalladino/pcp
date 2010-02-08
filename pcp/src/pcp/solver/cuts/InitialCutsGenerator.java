package pcp.solver.cuts;

import pcp.algorithms.block.BlockColorTable;
import pcp.interfaces.IConstraintsRequestor;
import pcp.model.Model;


public class InitialCutsGenerator {

	Model model;
	
	public InitialCutsGenerator(Model model) {
		this.model = model;
	}

	public void makeCuts(IConstraintsRequestor requestor) {
		new BlockColorTable(model).makeCuts(requestor);
	}

}
