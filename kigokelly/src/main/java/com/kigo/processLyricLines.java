/**
 * ~ PROJECT KIGO ~
 * The processLyricLines class provides utilities for processing and cleaning
 * song lyrics by removing unwanted content such as parenthetical text 
 * (e.g., backup vocals) and entire lines consisting only of non-lexical vocables 
 * (e.g., "oh, oh, oh" or "baby, baby"). 
 *
 * Key functionality includes:
 * - Removing content enclosed in parentheses.
 * - Filtering out lines that consist entirely of predefined vocables.
 *
 * Use Cases:
 * This class can be utilized in applications such as lyric analysis, text-based
 * lyric processing, or any scenario where clean and meaningful lyrics are required.
 *
 * Methods:
 * 1. main(ArrayList<String>): Processes a list of lyric lines and returns a cleaned list.
 *    - Removes content in parentheses.
 *    - Filters out lines entirely made of vocables.
 *
 * 2. removeContentInParentheses(String): Removes text enclosed in parentheses.
 *
 * 3. isVocableLine(String): Checks if a given line consists entirely of vocables.
 *
 * Implementation Details:
 * - Regular expressions are used for pattern matching, ensuring flexible 
 *   and efficient string processing.
 * - Vocables are predefined in a regex pattern and can be extended as needed.
 *
 * Example Input and Output:
 * Input:
 *     ["oh, oh, oh", "baby baby yeah", "This is a meaningful line", 
 *      "(Backup vocals) oh oh", "na na na"]
 * Output:
 *     ["This is a meaningful line"]
 *
 * Dependencies:
 * - Java Standard Library (java.util.ArrayList, java.io.IOException).
 */
package com.kigo;

import java.util.ArrayList;

public class processLyricLines {

	public static ArrayList<String> main(ArrayList<String> list) {
		
		ArrayList<String> noParenthesisLines = new ArrayList<>();
		ArrayList<String> fullyPolishedLines = new ArrayList<>();

		// Check if the list is empty
		if (!list.isEmpty()) {

			// For every line in the ArrayList
			for (String line : list) {

				// Remove back-up vocals/text in parenthesis
				String fixedLine = removeContentInParentheses(line);

				// Avoid adding blank lines
				if (!fixedLine.trim().isEmpty()) { // changed from isBlank()
					noParenthesisLines.add(fixedLine);
				}
			}
		}
		
		// Now to remove any lines consisting of non-lexical vocables
		for (String line : noParenthesisLines) {
			boolean isVocable = isVocableLine(line);
			// Avoid printing blank lines to the file
			if (!isVocable) {
				fullyPolishedLines.add(line);
			}
		}
		
		return fullyPolishedLines;
	} // end main

	private static String removeContentInParentheses(String input) {
		// Use a regular expression to remove content inside parentheses, including parentheses
		return input.replaceAll("\\(.*?\\)", "").trim();
	}
	
	private static boolean isVocableLine(String input) {
		// Define regex pattern for matching vocables
		String vocablesRegex = "^(\\b(oh|no|ah|la|na|yeah|yea|baby|whoa|doo|ba|sha)\\b[\\s,]*)+$";
        return input.trim().matches(vocablesRegex);
	} // end isVocableLine
	
} // end processLyricLines