package pcp.porta;

import pcp.Settings;
import pcp.model.BuilderStrategy;
import pcp.porta.processing.Cardinals;


public class Main {
	
	public static void main(String[] args) throws Exception {
		
		if (args.length == 0) {
			System.out.println("Must specify an action");
			return;
		}
		
		Settings.load("porta");
		
		BuilderStrategy strategy = BuilderStrategy.fromSettings();
		
		String basefilename = Settings.get().getPath("file.basename", "dir.porta");
		String detailedfilename = createFilename(basefilename, strategy, Settings.get().getBoolean("porta.projectColors"));
		String detailedname = createFilename(Settings.get().getString("file.basename"), strategy, Settings.get().getBoolean("porta.projectColors"));
		String specificname = detailedfilename + "." + Settings.get().getString("file.specificname");
		
		String graphfile = basefilename + ".in";
		String ieqfile = detailedfilename + ".ieq";
		String poifile = detailedfilename + ".poi";
		String traffile = detailedfilename + ".poi.ieq";
		String modelfile = detailedfilename + ".model";
		String specificmodelfile = specificname + ".model";
		String specificportafile = specificname + ".ieq";
		
		Integer nodeCount = Settings.get().getInteger("porta.nodeCount");
		Integer colorCount = Settings.get().getInteger("porta.colorCount");
		
		Cardinals cardinals = new Cardinals(nodeCount, colorCount);
		
		Boolean preprocess = Settings.get().getBoolean("writer.preprocess");

		String action = args[0];
		if (action.equalsIgnoreCase("--ieq")) {
			System.out.println("Writing ieq file from input graph");
			WriteIeq.write(graphfile, ieqfile, preprocess, strategy);
		} else if (action.equalsIgnoreCase("--process")) {
			System.out.println("Processing ieq file");
			ProcessIeq.process(graphfile, traffile, modelfile, cardinals, strategy);
		} else if (action.equalsIgnoreCase("--name")) {
			System.out.println(detailedname);
		} else if (action.equalsIgnoreCase("--poi")) {
			System.out.println("Writing poi file from input graph");
			WritePoi.write(graphfile, poifile, strategy);
		} else if (action.equalsIgnoreCase("--translate")) {
			System.out.println("Translating from specific model file to porta file");
			TranslateModel.translate(specificmodelfile, specificportafile, strategy);
		} else {
			System.err.println("Invalid action specified " + args[0]);
		}
	}

	private static String createFilename(String basefilename, BuilderStrategy strategy, Boolean projectColors) {
		String portanames = Settings.get().getString("porta.filenames");
		Integer colorcount = Settings.get().getInteger("porta.colorCount");
		
		if ("full".equals(portanames)) {
			String ret = basefilename + "." + strategy.shortId();
			if (projectColors) ret += ".proj";
			ret += ".C" + colorcount.toString();
			return ret;
		} else if ("colors".equals(portanames)){
			return basefilename + ".C" + colorcount.toString();
		} else {
			return basefilename;
		}
	}

	
	
}
