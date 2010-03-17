package porta.processing;

import porta.model.BaseModel;

public interface IProcessor {

	void process(BaseModel<?, ?, ?> model);
	
}
