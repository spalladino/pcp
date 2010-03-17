package porta;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;


public class ReadDim {

	public static void read(String specificportafile) {
		FileNotFoundException notFound = null;
		int counter = 1;
		while (notFound == null) {
			try {
				String file = specificportafile + counter + ".poi";
				FileInputStream stream = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String line = null;
				while ((line = reader.readLine()) != null && !line.startsWith("DIMENSION OF THE POLYHEDRON"));
				String dim = line.substring(30);
				System.out.println("Inequality " + counter + " dimension: " + dim);
				counter++;
			} catch (FileNotFoundException ex) {
				notFound = ex;
			} catch (Exception ex2) {
				System.err.println(ex2);
				break;
			}
		}
		
	}
	
}
