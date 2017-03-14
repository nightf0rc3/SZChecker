package de.nightf0rc3.szchecker;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Ueberprueft eine als Argument uebergebene Datei auf Sonderzeichen
 *
 */
public class SonderzeichenChecker {
	
	private char[] badChars = {'ä', 'Ä', 'ö', 'Ö', 'ü', 'Ü', 'ß'};
	
	
	/**
	 * ueberprueft eine übergebene Datei auf Sonderzeichen
	 * @param filename
	 */
	public void checkFile(String filename) {
		String content= "";
		try (FileInputStream fis = new FileInputStream(filename)) {
			int input;
			while ((input = fis.read()) != -1) {
				content += (char) input;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Die angegebene Datei konnte nicht gefunden werden.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] lines = content.split("\r\n|\r|\n");
		boolean fine = true;
		System.out.println("Datei: " + filename + " (" + lines.length + " lines)");
		for (int i = 0; i < lines.length; i++) {
			for (int j = 0; j < badChars.length; j++) {
				if (lines[i].contains("" + badChars[j])) {
					int index = lines[i].indexOf(badChars[j]);
					System.out.println("Bad Char \"" + lines[i].charAt(index) + "\" at line " + (i+1));
					fine = false;
				}
			}
		}
		if (fine) {
			System.out.println("Es wurden keine Sonderzeichen gefunden!");
		}
	}

	public static void main(String[] args) {
		try {
			SonderzeichenChecker sc = new SonderzeichenChecker();
			sc.checkFile(args[0]);
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Es wurde keine Datei uebergeben!");
		}

	}

}
