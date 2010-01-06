package pcp.porta.processing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pcp.porta.model.Constraint;
import pcp.porta.model.Family;
import pcp.porta.model.Model;


public abstract class ConstraintsMatcher {

	abstract boolean match(Constraint c1, Constraint c2);
	
	abstract boolean process(Constraint c);
	
	boolean repeatProcessed = false;
	
	final Model model;
	final Set<Constraint> handled = new HashSet<Constraint>();
	private final List<Constraint> cs;
	
	Family current;
	
	public ConstraintsMatcher(Model model) {
		this.model = model;
		this.cs= model.getConstraints();
	}
	
	public void run() {
		for (int i = 0; i < cs.size(); i++) {
			if (repeatProcessed || !handled.contains(cs.get(i))) {
				current = model.createFamily(cs.get(i));
				if (process(cs.get(i))) {
					boolean hasmatches = false;
					for (int j = i+1; j < cs.size(); j++) {
						if (repeatProcessed || !handled.contains(cs.get(j))) {
							if (match(cs.get(i), cs.get(j))) {
								handled.add(cs.get(j));
								hasmatches = true;
							}
						}
					}
					if (!hasmatches) {
						model.removeFamily(current);
					} else {
						handled.add(cs.get(i));
					}
				} else {
					model.removeFamily(current);
				}
			}
		}
	}
	
}
