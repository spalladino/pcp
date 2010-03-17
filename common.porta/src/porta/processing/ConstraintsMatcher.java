package porta.processing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import porta.base.BaseParameters;
import porta.model.BaseConstraint;
import porta.model.BaseFamily;
import porta.model.BaseModel;

public abstract class ConstraintsMatcher<CONSTRAINT extends BaseConstraint, FAMILY extends BaseFamily, PARAMS extends BaseParameters> {

	protected abstract boolean match(CONSTRAINT c1, CONSTRAINT c2);
	
	protected abstract boolean process(CONSTRAINT c);
	
	protected boolean repeatProcessed = false;
	
	protected final BaseModel<CONSTRAINT, FAMILY, PARAMS> model;
	protected final Set<CONSTRAINT> handled = new HashSet<CONSTRAINT>();
	protected final List<CONSTRAINT> cs;
	
	protected FAMILY current;
	
	public ConstraintsMatcher(BaseModel<CONSTRAINT, FAMILY, PARAMS> model) {
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
