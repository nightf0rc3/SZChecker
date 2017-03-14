package de.nightf0rc3.szchecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Checks a given file for umlauts and replaces them if wanted.
 */
public class SonderzeichenChecker {
	
	private String[] goodChars = {"ae", "Ae", "oe", "Oe", "ue", "Ue", "ss"};
	private String[] badChars = {"ä", "Ä", "ö", "Ö", "ü", "Ü", "ß"};
	private String filename;
	private String newFilename;
	private boolean createFile;
	
	
	/**
	 * creates a new instance of the SonderzeichenChecker.
	 * @param filename path to the file which should be checked
	 * @param correctFile should the umlauts be replaced
	 */
	public SonderzeichenChecker(String filename, boolean correctFile) {
		String[] parts = filename.split("\\.(?=[^\\.]+$)");
		this.filename = filename;
		try {
			this.newFilename = parts[0] + "_clean." + parts[1];
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			this.newFilename = filename + "_clean";
		}
		this.createFile = correctFile;
	}
	
	/**
	 * reads the file given in the attribute file.
	 * @return content of file
	 */
	private String readFile() {
		String content= "";
		try (FileInputStream fis = new FileInputStream(filename)) {
			int input;
			while ((input = fis.read()) != -1) {
				content += (char) input;
			}
		} catch (FileNotFoundException e) {
			System.out.println("The supplied file could not be found!");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * writes given content to file.
	 * @param content corrected string
	 */
	public void writeFile(String content){
		File file = new File(this.newFilename);
		try (FileWriter fw = new FileWriter(file)){
			fw.write(content);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * checks a file for umlauts and replaces them.
	 */
	public void checkFile() {
		String text = this.readFile();
		String[] lines = text.split("\r\n|\r|\n");
		boolean fine = true;
		System.out.println("File: " + filename + " (" + lines.length + " lines)");
		for (int i = 0; i < lines.length; i++) {
			for (int j = 0; j < badChars.length; j++) {
				if (lines[i].contains("" + badChars[j])) {
					System.out.println("Bad Char '" + badChars[j] + "' at line " + (i+1));
					fine = false;
					if (createFile) {
						text = text.replace(badChars[j], goodChars[j]);
					}
				}
			}
		}
		if (fine) {
			System.out.println("No umlauts found!");
		} else if (createFile) {
			this.writeFile(text);
			System.out.println("Wrote clean version to " + newFilename);
		}
	}

	
	/**
	 * main method.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args.length == 2 && args[0].equals("-c")) {
				SonderzeichenChecker sc = new SonderzeichenChecker(args[1], true);
				sc.checkFile();
			} else if (args.length == 1){
				SonderzeichenChecker sc = new SonderzeichenChecker(args[0], false);
				sc.checkFile();
			} else {
				System.out.println("Usage: szChecker (-c) file");
			}
		} else {
			System.out.println("Usage: szChecker (-c) file");
		}
	}

}
