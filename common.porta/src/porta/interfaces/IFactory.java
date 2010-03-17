package porta.interfaces;

import porta.BaseParameters;
import porta.io.ModelWriter;
import porta.io.PortaWriter;
import porta.model.BaseConstraint;
import porta.model.BaseFamily;
import porta.model.Model;
import porta.poi.IPointsGenerator;
import porta.processing.IProcessor;
import porta.processing.ITranslator;


public interface IFactory {

	IPointsGenerator createPointsGenerator(IEntity graph) throws Exception;
	
	/**
	 * Creates a new translator instance that can translate between porta and model variable names. 
	 * @return a new translator instance
	 */
	ITranslator createTranslator();
	
	IProcessor createProcessor(IEntity graph, BaseParameters c);

	<C extends BaseConstraint, F extends BaseFamily, I extends BaseParameters> Model<C,F,I> createModel(I c);
	
	ModelWriter createModelWriter(Model<?, ?, ?> model);

	PortaWriter createPortaWriter(Model<?, ?, ?> model);

	/**
	 * Returns parameters needed for this problem (such as node count).
	 * Implementors should create an instance when a new entity is read and return it.
	 * @see IFactory#readEntity(String, Boolean)
	 * @return
	 */
	BaseParameters getParameters();

	/**
	 * Given an input file, reads a certain entity (such as a graph)
	 * @param filename input file containing the entity
	 * @param preprocess whether to preprocess the entity or leave it as is
	 * @return the entity represented in the file
	 * @throws Exception
	 */
	IEntity readEntity(String filename, Boolean preprocess) throws Exception;

	/**
	 * Generates a Model with all constraints given an input entity (such as a graph).
	 * @param graph input entity to generate the model for
	 * @return LP model for this entity
	 * @throws Exception
	 */
	Model<?, ?, ?> generateModel(IEntity graph) throws Exception;

	Model<?, ?, ?> readModel(String filename) throws Exception;
}
