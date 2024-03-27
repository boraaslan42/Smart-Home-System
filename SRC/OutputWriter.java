import java.io.*;

public class OutputWriter {
	private static BufferedWriter writer;

	/**
	 * Writes a line to the output file
	 * @param text
	 */

	public static void write(String text) {
		try {
			if (writer == null) {
				writer = new BufferedWriter(new FileWriter(Main.outputFileName));
			}
			writer.write(text);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Closes the output file
	 */
	public static void close() {
		try {
			if (writer != null) {
				writer.close();
				writer = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
