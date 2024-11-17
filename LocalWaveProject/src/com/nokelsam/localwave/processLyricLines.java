package com.nokelsam.localwave;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class processLyricLines {

	public static void main(String[] args) throws IOException {

		// Set up/open nonduplicatelyrics file for further processing of lyrics.
		final String TEST_FILE = "non-duplicate-lyrics.txt";
		File file = new File(TEST_FILE);
		Scanner scanner = new Scanner(file);

		ArrayList<String> lyricLines = new ArrayList<>();

		String line = "";
		// While lines remain
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			// Add entire line to the ArrayList.
			lyricLines.add(line);
		}
		scanner.close();

		// Output file
		final String LYRICS_OUT = "non-parenthesis-lyrics.txt";
		FileWriter outWriter = new FileWriter(LYRICS_OUT);

		// Check if the list is empty
		if (!lyricLines.isEmpty()) {

			// For every line in the ArrayList
			for (String item : lyricLines) {

				// Remove back-up vocals in parenthesis
				String fixedLine = removeContentInParentheses(item);

				// Avoid printing blank lines to the file
				if (!fixedLine.isBlank()) {
					outWriter.write(fixedLine + "\n");
				}
			}
		} else {
			System.out.println("The list is empty.");
		}

		outWriter.close();
	} // end main

	public static String removeContentInParentheses(String input) {
		// Use a regular expression to remove content inside parentheses, including
		// parentheses
		return input.replaceAll("\\(.*?\\)", "").trim();
	}
} // end processLyricLines