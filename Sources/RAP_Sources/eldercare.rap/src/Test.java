import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import au.com.bytecode.opencsv.CSVWriter;
import eldercare.rap.utilities.CSVReader;

public class Test {

	public static void main(String[] args) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(new File(
				"C:\\temp\\temporary1.csv")));

		String[] line;
		while ((line = reader.readNext()) != null) {
			File currentInstance = new File("C:/temp/currentInstance.csv");
			CSVWriter currentCSVW = new CSVWriter(new PrintWriter(
					new FileWriter(currentInstance, false)));
			currentCSVW.writeNext(line);
			currentCSVW.close();
			// DataCollectionManager.getInstance().dataUpdated(strings);
			/*
			 * for (int i = 0; i < strings.length - 1; i++) { String[] split =
			 * strings[i].split("\\("); String value = split[0]; String coord =
			 * split[1].substring(0, split[1].length() - 1); String[]
			 * coordinates = coord.split(","); int row =
			 * Integer.parseInt(coordinates[0]); int column =
			 * Integer.parseInt(coordinates[1]); sum[row * 8 + column] = sum[row
			 * * 8 + column] + Integer.parseInt(value); }
			 */

		}

	}
}
