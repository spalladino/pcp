package porta.io;

import java.io.PrintStream;

import porta.model.BaseFamily;
import porta.model.BaseVariable;
import porta.model.BaseModel;
import porta.processing.ITranslator;
import exceptions.AlgorithmException;


public abstract class ModelWriter<VARIABLE extends BaseVariable> extends PortaWriter<VARIABLE> {

	public ModelWriter(BaseModel<?, ?, ?> model, ITranslator<VARIABLE> translator) {
		super(model, translator);
	}
	
	@Override
	void innerWriteIeq(PrintStream stream) throws AlgorithmException {
		writeCardinals(stream);
		writeInequalities(stream);
		writeFamilies(stream);
	}
	
	void writeFamilies(PrintStream stream) {
		stream.print('\n');
		for (BaseFamily f : model.getFamilies()) {
			if (!(skipFamily(f))) {
				stream.print(f.toString());
				stream.print('\n');
			}
		}
	}

	protected abstract boolean skipFamily(BaseFamily family);
	
	protected abstract void writeCardinals(PrintStream stream);
	
}
