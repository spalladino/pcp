package porta.interfaces;

import porta.base.BaseParameters;
import porta.io.ModelWriter;
import porta.io.PortaWriter;
import porta.model.BaseConstraint;
import porta.model.BaseFamily;
import porta.model.BaseVariable;
import porta.model.BaseModel;
import porta.poi.IPointsGenerator;
import porta.processing.IProcessor;
import porta.processing.ITranslator;


public interface IFactory<ENTITY,
	CONSTRAINT extends BaseConstraint,
	PARAMS extends BaseParameters,
	FAMILY extends BaseFamily,
	VARIABLE extends BaseVariable,
	MODEL extends BaseModel<CONSTRAINT, FAMILY, PARAMS>
	> {

	IPointsGenerator createPointsGenerator(ENTITY graph) throws Exception;
	
	/**
	 * Creates a new translator instance that can translate between porta and model variable names. 
	 * @return a new translator instance
	 */
	ITranslator<VARIABLE> createTranslator();
	
	IProcessor createProcessor(ENTITY graph, PARAMS c);

	MODEL createModel(PARAMS c);
	
	ModelWriter<VARIABLE> createModelWriter(MODEL model);

	PortaWriter<VARIABLE> createPortaWriter(MODEL model);

	/**
	 * Returns parameters needed for this problem (such as node count).
	 * @return
	 */
	PARAMS getParameters();

	/**
	 * Given an input file, reads a certain entity (such as a graph)
	 * @param filename input file containing the entity
	 * @param preprocess whether to preprocess the entity or leave it as is
	 * @return the entity represented in the file
	 * @throws Exception
	 */
	ENTITY readEntity(String filename, Boolean preprocess) throws Exception;

	/**
	 * Generates a Model with all constraints given an input entity (such as a graph).
	 * @param graph input entity to generate the model for
	 * @return LP model for this entity
	 * @throws Exception
	 */
	MODEL generateModel(ENTITY graph) throws Exception;

	MODEL readModel(String filename) throws Exception;
}
