package pcp.porta;

import java.io.FileInputStream;

import pcp.algorithms.Preprocessor;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.model.BuilderStrategy;
import pcp.model.ModelBuilder;
import pcp.porta.model.PcpModel;
import pcp.porta.model.Variable;
import pcp.porta.model.ilog.MockMPModeler;
import pcp.porta.parser.ModelParser;
import pcp.porta.poi.PointsGenerator;
import pcp.porta.processing.Processor;
import pcp.porta.processing.Translator;
import porta.BaseParameters;
import porta.base.EntityHolder;
import porta.interfaces.IEntity;
import porta.interfaces.IFactory;
import porta.io.ModelWriter;
import porta.io.PortaWriter;
import porta.model.BaseConstraint;
import porta.model.BaseFamily;
import porta.model.Model;
import porta.poi.IPointsGenerator;
import porta.processing.IProcessor;
import porta.processing.ITranslator;


public class Factory implements IFactory {

	boolean projectColors;
	BuilderStrategy strategy;
	PcpCardinals parameters;
	
	public Factory(boolean projectColors, BuilderStrategy strategy) {
		this.projectColors = projectColors;
		this.strategy = strategy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <C extends BaseConstraint, F extends BaseFamily, I extends BaseParameters> Model<C, F, I> createModel(I c) {
		return (Model<C, F, I>) new PcpModel((PcpCardinals) c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Model readModel(String filename) throws Exception {
		return new ModelParser(new FileInputStream(filename)).model();
	}
	
	
	@Override
	public ModelWriter createModelWriter(Model<?, ?, ?> model) {
		return new pcp.porta.io.ModelWriter((PcpModel) model);
	}

	@Override
	public IPointsGenerator createPointsGenerator(IEntity graph) throws Exception {
		return new PointsGenerator(toGraph(graph), parameters, strategy);
	}

	@Override
	public PortaWriter createPortaWriter(Model<?, ?, ?> model) {
		return new pcp.porta.io.PortaWriter((PcpModel) model);
	}

	@Override
	public IProcessor createProcessor(IEntity graph, BaseParameters c) {
		return new Processor(toGraph(graph), (PcpCardinals) c, strategy);
	}

	@Override
	public ITranslator<Variable> createTranslator() {
		return new Translator(getParameters());
	}

	@Override
	public PcpCardinals getParameters() {
		if (this.parameters == null) {
			this.parameters = new PcpCardinals();
		}
		return this.parameters;
	}

	@Override
	public IEntity readEntity(String filename, Boolean preprocess) throws Exception {
		PartitionedGraphBuilder graphb = pcp.Factory.get().getGraphBuilder(filename);
		if (preprocess) { 
			graphb = new Preprocessor(graphb).preprocess();
			System.out.println("Preprocessed graph has " + graphb.getNodes().length + " nodes and " + graphb.getEdges().length + " edges.");
		}  else {
			System.out.println("Graph has " + graphb.getNodes().length + " nodes and " + graphb.getEdges().length + " edges.");
		}
		
		PartitionedGraph graph = graphb.getGraph();
		buildParameters(graph);
		return asEntity(graph);
	}

	@Override
	public PcpModel generateModel(IEntity graph) throws Exception {
		MockMPModeler modeler = new MockMPModeler();
		ModelBuilder builder = new ModelBuilder((PartitionedGraph) toGraph(graph), modeler);
		builder.buildModel(strategy);
		return modeler.asModel(this.createTranslator());
	}

	private BaseParameters buildParameters(IPartitionedGraph graph) {
		return parameters = new PcpCardinals(graph.N());
	}
	
	@SuppressWarnings("unchecked")
	private IPartitionedGraph toGraph(IEntity entity) {
		return ((EntityHolder<IPartitionedGraph>) entity).get();
	}
	
	@SuppressWarnings("unused")
	private IEntity asEntity(IPartitionedGraph graph) {
		return new EntityHolder<IPartitionedGraph>(graph);
	}

	
	
}
