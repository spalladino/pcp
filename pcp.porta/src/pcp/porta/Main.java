package pcp.porta;

import pcp.model.BuilderStrategy;
import props.Settings;


public class Main {
	
	public static void main(String[] args) throws Exception {
		Settings.load("porta");
		boolean projectColors = Settings.get().getBoolean("porta.projectColors");
		BuilderStrategy strategy = BuilderStrategy.fromSettings();
		
		FilesHandler handler = new FilesHandler(projectColors, strategy);
		Factory factory = new Factory(projectColors, strategy);
		
		porta.Main main = new porta.Main(handler, factory);
		main.main(args[0]);
	}
}
