package pcp.porta;

import pcp.model.BuilderStrategy;
import porta.interfaces.IFilesHandler;
import props.Settings;


public class FilesHandler implements IFilesHandler {

	boolean projectColors;
	BuilderStrategy strategy;
	
	public FilesHandler(boolean projectColors, BuilderStrategy strategy) {
		this.projectColors = projectColors;
		this.strategy = strategy;
	}

	@Override
	public String createFilename(String basefilename) {
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
