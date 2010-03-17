package porta.processing;

import porta.base.BaseParameters;
import porta.model.BaseVariable;

public interface ITranslator<V extends BaseVariable> {
	
	V convertNameToModel(String name);
	
	V convertPortaToModel(int index);
	
	int convertModelToPorta(V var);

	BaseParameters getParameters();
	
}