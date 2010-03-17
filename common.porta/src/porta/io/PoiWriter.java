package porta.io;

import java.io.PrintStream;
import java.util.List;


public class PoiWriter {
	
	private List<int[]> points;
	private int dimension;

	public PoiWriter(int dimension, List<int[]> points) {
		this.dimension = dimension;
		this.points = points;
	}
	
	public void write(String filename) throws Exception {
		try {
			PrintStream stream = new PrintStream(filename);
			write(stream);
			stream.close();
		} catch (Exception ex) {
			System.err.println("Error trying to print ieq file to " + filename);
			System.err.println(ex.getMessage());
			throw ex;
		}
	}

	public void write(PrintStream stream) {
		stream.print("DIM =");
		stream.print(dimension);
		stream.print("\n\nCONV_SECTION");
		
		int index = 1;
		for (int[] point : points) {
			stream.print("\n(");
			stream.print(index);
			stream.print(")");
			
			for (int x : point) {
				stream.print(" ");
				stream.print(x);
			}
			
			index++;
		}
		
		stream.print("\n\nEND\n");
	}
	
}
