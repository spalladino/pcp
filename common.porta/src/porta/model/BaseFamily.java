package porta.model;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseFamily extends BaseConstraint {

	List<BaseConstraint> represented = new ArrayList<BaseConstraint>();
	
	public void representingConstraint(BaseConstraint c) {
		represented.add(c);
	}
	
	public List<BaseConstraint> getRepresented() {
		return represented;
	}
}
