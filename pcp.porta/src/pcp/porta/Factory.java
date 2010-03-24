package pcp.porta;

import java.io.FileInputStream;

import pcp.algorithms.Preprocessor;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.model.BuilderStrategy;
import pcp.model.ModelBuilder;
import pcp.porta.model.Constraint;
import pcp.porta.model.Family;
import pcp.porta.model.Model;
import pcp.porta.model.Variable;
import pcp.porta.parser.ModelParser;
import pcp.porta.poi.PointsGenerator;
import pcp.porta.processing.Processor;
import pcp.porta.processing.Translator;
import porta.base.BaseParameters;
import porta.interfaces.IFactory;
import porta.io.ModelWriter;
import porta.io.PortaWriter;
import porta.model.ilog.MockMPModeler;
import porta.poi.IPointsGenerator;
import porta.processing.IProcessor;
import porta.processing.ITranslator;


public class Factory implements IFactory<
	IPartitionedGraph,
	Constraint,
	Parameters,
	Family,
	Variable,
	Model> {

	boolean projectColors;
	BuilderStrategy strategy;
	Parameters parameters;
	
	public Factory(boolean projectColors, BuilderStrategy strategy) {
		this.projectColors = projectColors;
		this.strategy = strategy;
	}

	@Override
	public Model createModel(Parameters c) {
		return new Model(c);
	}

	@Override
	public Model readModel(String filename) throws Exception {
		return (Model) new ModelParser(new FileInputStream(filename)).model();
	}
	
	
	@Override
	public ModelWriter<Variable> createModelWriter(Model model) {
		return new pcp.porta.io.ModelWriter((Model) model);
	}

	@Override
	public IPointsGenerator createPointsGenerator(IPartitionedGraph graph) throws Exception {
		return new PointsGenerator(graph, parameters, strategy);
	}

	@Override
	public PortaWriter<Variable> createPortaWriter(Model model) {
		return new pcp.porta.io.PortaWriter(model);
	}

	@Override
	public IProcessor createProcessor(IPartitionedGraph graph, Parameters c) {
		return new Processor(graph, c, strategy);
	}

	@Override
	public ITranslator<Variable> createTranslator() {
		return new Translator(getParameters());
	}

	@Override
	public Parameters getParameters() {
		if (this.parameters == null) {
			this.parameters = new Parameters();
		}
		return this.parameters;
	}

	@Override
	public IPartitionedGraph readEntity(String filename, Boolean preprocess) throws Exception {
		PartitionedGraphBuilder graphb = pcp.Factory.get().getGraphBuilder(filename);
		if (preprocess) { 
			graphb = new Preprocessor(graphb).preprocess();
			System.out.println("Preprocessed graph has " + graphb.getNodes().length + " nodes and " + graphb.getEdges().length + " edges.");
		}  else {
			System.out.println("Graph has " + graphb.getNodes().length + " nodes and " + graphb.getEdges().length + " edges.");
		}
		
		PartitionedGraph graph = graphb.getGraph();
		buildParameters(graph);
		return graph;
	}

	@Override
	public Model generateModel(IPartitionedGraph graph) throws Exception {
		MockMPModeler<Model> modeler = new MockMPModeler<Model>(this);
		ModelBuilder builder = new ModelBuilder((PartitionedGraph) graph, modeler);
		ColoringAlgorithm coloring = pcp.Factory.get().coloring(strategy.getColoring(), graph);
		builder.buildModel(strategy, coloring);
		return modeler.asModel(this.createTranslator(), getParameters());
	}

	private BaseParameters buildParameters(IPartitionedGraph graph) {
		return parameters = new Parameters(graph.N());
	}
	
}
