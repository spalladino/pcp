package porta.processing;

import porta.model.Model;

public interface IProcessor {

	void process(Model<?, ?, ?> model);
	
}
