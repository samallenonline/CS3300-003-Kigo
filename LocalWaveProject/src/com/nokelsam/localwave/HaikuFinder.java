package com.nokelsam.localwave;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

public class HaikuFinder {
	public static void main(String[] args) throws IOException {

		final String TEST_FILE = "rap-god.txt";

		File file = new File(TEST_FILE);
		Scanner scanner = new Scanner(file);

		ArrayList<String> thisLineArrayList = new ArrayList<>();
		ArrayList<String> sevenSyllableLines = new ArrayList<>();
		ArrayList<String> fiveSyllableLines = new ArrayList<>();

		int lineCount = 0;
		int fourSyllableLineCount = 0;
		int fiveSyllableLineCount = 0;
		int sixSyllableLineCount = 0;
		int sevenSyllableLineCount = 0;

		// Walks through words in a file checking for presence of a haiku.
		while (scanner.hasNextLine()) {

			// Clear the ArrayList for each new line
			thisLineArrayList.clear();

			// Get next word in the file
			String currentLine = scanner.nextLine();

			// Split the current line into words by whitespace
			String[] words = currentLine.split("\\s+");

			// System.out.println("currentLine After split: " + currentLine);

			// Add each word from the line into the ArrayList
			for (String word : words) {
				thisLineArrayList.add(word);
			}

			// Count syllables
			lineCount = countSyllablesInLine(thisLineArrayList);

			// If lineCount == 4
			if (lineCount == 4) {
				fourSyllableLineCount++;
				// Call addOneSyllable on thisLineArrayList to expand conjunctions
				thisLineArrayList = addOneSyllable(thisLineArrayList);
			}
			// If line matches requirements to be a haiku line,
			else if (lineCount == 5) {
				// Add to Arraylist & store for later use.
				if (!fiveSyllableLines.contains(currentLine)) {
					fiveSyllableLines.add(currentLine);
					fiveSyllableLineCount++;
					// System.out.println("Line: " + currentLine + " " + lineCount);
				}
			} 
			else if (lineCount == 7) {
				if (!sevenSyllableLines.contains(currentLine)) {
					sevenSyllableLines.add(currentLine);
					sevenSyllableLineCount++;
					// System.out.println("Line: " + currentLine + " " + lineCount);
				}
			}
			// If count = 8 and the first word is "and"
			else if (lineCount == 8 && words[0].toLowerCase().equals("and")) {
				// Remove the word "and" to reach 7 syllables and increase # usable lines

				String modifiedLine = removeWordFromLine(thisLineArrayList, "and");
				// System.out.println("Modified line: " + modifiedLine);

				// Recalculate syllables
				int newSyllableCount = countSyllablesInLine(thisLineArrayList);
				if ( (newSyllableCount == 7) && (!sevenSyllableLines.contains(modifiedLine)) ) {
					sevenSyllableLines.add(modifiedLine);
				}

			}
			// If count = 8 and line conatins the word "of"
			else if ((lineCount == 8) && (currentLine.contains("the"))) {
				// Remove "of" to fit 7 syllables
				String modifiedLine = removeWordFromLine(thisLineArrayList, "the");
				// System.out.println("Modified line: " + modifiedLine);

				// Recalculate syllables
				int newSyllableCount = countSyllablesInLine(thisLineArrayList);
				if ( (newSyllableCount == 7) && (!sevenSyllableLines.contains(modifiedLine)) ) {
					sevenSyllableLines.add(modifiedLine);
				}
			} // end if else-if
		} // end while
		System.out.println("\n");
		System.out.println("Contents of 5 Syllable ArrayList:");
		printArrayList(fiveSyllableLines);
		System.out.println("");
		System.out.println("Contents of 7 Syllable ArrayList:");
		printArrayList(sevenSyllableLines);
		
		System.out.println("\n");
		System.out.println("~ Haiku ~");
		String[] haiku = putRandomHaikuTogetherFromArrayLists(fiveSyllableLines, sevenSyllableLines);
		printHaikuStringArray(haiku);
		
		haiku = putRandomHaikuTogetherFromArrayLists(fiveSyllableLines, sevenSyllableLines);
		printHaikuStringArray(haiku);
		
		haiku = putRandomHaikuTogetherFromArrayLists(fiveSyllableLines, sevenSyllableLines);
		printHaikuStringArray(haiku);
		
	} // end main

	public static void printHaikuStringArray(String[] haiku) {	
		System.out.println("\n" + haiku[0]);
		System.out.println(haiku[1]);
		System.out.println(haiku[2]);		
	} // end printHaikuStringArray
	
	public static String[] putRandomHaikuTogetherFromArrayLists(ArrayList<String> five, ArrayList<String> seven) {
		
		String[] haiku = new String[3];
		
		int numFiveSyllableLines = five.size();
		int numSevenSyllableLines = seven.size();
		
		int firstRandomFiveLine = (int) (Math.random() * numFiveSyllableLines);
		int randomSevenLine = (int) (Math.random() * numSevenSyllableLines);
		int secondRandomFiveLine = (int) (Math.random() * numFiveSyllableLines);
		
		if (!five.isEmpty()) {
			haiku[0] = five.get(firstRandomFiveLine);
		}
		if (!seven.isEmpty()) {
			haiku[1] = seven.get(randomSevenLine);
		}
		if (!five.isEmpty()) {
			haiku[2] = five.get(secondRandomFiveLine);
		}				
		return haiku;
	} // end putTogetherHaiku
	
	
	private static ArrayList<String> addOneSyllable(ArrayList<String> list) {
		// Use an iterator to find potential words to extend
		Iterator<String> iterator = list.iterator();
		ArrayList<String> updatedList = new ArrayList<>();

		while (iterator.hasNext()) {
			String word = iterator.next();

			switch (word.toLowerCase()) { // Convert to lowercase for case-insensitive matching
			case "i'm":
				updatedList.add("I");
				updatedList.add("am");
				break;
			case "you're":
				updatedList.add("you");
				updatedList.add("are");
				break;
			case "we're":
				updatedList.add("we");
				updatedList.add("are");
				break;
			case "they're":
				updatedList.add("they");
				updatedList.add("are");
				break;
			case "he's":
				updatedList.add("he");
				updatedList.add("is");
				break;
			case "she's":
				updatedList.add("she");
				updatedList.add("is");
				break;
			case "it's":
				updatedList.add("it");
				updatedList.add("is");
				break;
			case "i've":
				updatedList.add("I");
				updatedList.add("have");
				break;
			case "you've":
				updatedList.add("you");
				updatedList.add("have");
				break;
			case "we've":
				updatedList.add("we");
				updatedList.add("have");
				break;
			case "they've":
				updatedList.add("they");
				updatedList.add("have");
				break;
			case "don't":
				updatedList.add("do");
				updatedList.add("not");
				break;
			case "doesn't":
				updatedList.add("does");
				updatedList.add("not");
				break;
			case "didn't":
				updatedList.add("did");
				updatedList.add("not");
				break;
			case "i'll":
				updatedList.add("I");
				updatedList.add("will");
				break;
			case "you'll":
				updatedList.add("you");
				updatedList.add("will");
				break;
			case "we'll":
				updatedList.add("we");
				updatedList.add("will");
				break;
			case "they'll":
				updatedList.add("they");
				updatedList.add("will");
				break;
			case "i'd":
				updatedList.add("I");
				updatedList.add("would"); // Default to "would" but can adjust based on context
				break;
			case "you'd":
				updatedList.add("you");
				updatedList.add("would");
				break;
			case "he'd":
				updatedList.add("he");
				updatedList.add("would");
				break;
			case "she'd":
				updatedList.add("she");
				updatedList.add("would");
				break;
			case "they'd":
				updatedList.add("they");
				updatedList.add("would");
				break;
			case "gonna":
				updatedList.add("going");
				updatedList.add("to");
				break;
			case "wanna":
				updatedList.add("want");
				updatedList.add("to");
				break;
			case "lemme":
				updatedList.add("let");
				updatedList.add("me");
				break;
			case "gimme":
				updatedList.add("give");
				updatedList.add("me");
				break;
			case "y'all":
				updatedList.add("you");
				updatedList.add("all");
				break;
			default:
				// If no match, add the word as is
				updatedList.add(word);
				break;
			}
		} // end while iterator

		return updatedList;
	} // end tryToAddOneSyllable

	private static String removeWordFromLine(ArrayList<String> thisLine, String wordToRemove) {
		// Use an iterator to safely remove the word while iterating
		Iterator<String> iterator = thisLine.iterator();

		while (iterator.hasNext()) {
			String word = iterator.next();

			if (word.equalsIgnoreCase(wordToRemove)) {
				iterator.remove(); // Remove the word from the ArrayList
				break;
			}
		}

		// Rebuild the line from the modified ArrayList
		return String.join(" ", thisLine);
	} // end removeWordFromLine

	public static void printArrayList(ArrayList<String> list) {
		// Check if the list is empty
		if (list.isEmpty()) {
			System.out.println("The list is empty.");
			return;
		}

		// Print each string in the list
		for (String item : list) {
			System.out.println(item);
		}
	}

	/*
	 * Method: findHaikus --------------------------- Reads words from a specified
	 * text file, processes them for syllable counts, and identifies potential
	 * haikus (poems with a 5-7-5 syllable structure).
	 * 
	 * Steps: - Reads words from the file and maintains a running total of
	 * syllables. - Resets processing if the syllable count exceeds 17. - Checks for
	 * potential haikus when the syllable count reaches exactly 17. - Passes the
	 * words to checkForHaiku for further validation.
	 * 
	 * Parameters: - filename (String): The name of the file containing the words to
	 * analyze.
	 * 
	 * Throws: - IOException if the specified file cannot be read.
	 */
	public static void findHaikus(String filename) throws IOException {
		// final String TEST_FILE = "rap-god-polished.txt";
		// final String TEST_FILE = "joji-your-man_lryics.txt";
		final String TEST_FILE = "car-radio-lyrics.txt";

		File file = new File(TEST_FILE);
		Scanner scanner = new Scanner(file);

		ArrayList<String> currentWords = new ArrayList<>();
		int syllableCount = 0;

		// Walks through words in a file checking for presence of a haiku.
		while (scanner.hasNextLine()) {

			// Get next word in the file
			String word = scanner.next();
			// Add to the ArrayList
			currentWords.add(word);
			// Update syllable count with current word's count
			syllableCount += SyllableCounter1.countSyllables(word);

			// If word caused syllable count overflow (17 syllables 5+7+5), then start over
			if (syllableCount > 17) {
				currentWords.clear();
				syllableCount = 0;
			}

			// If we hit exactly 17 syllables, check if it forms a haiku
			if (syllableCount == 17) {
				checkForHaiku(currentWords);
				// Reset for next attempt
				currentWords.clear();
				syllableCount = 0;
			}
		}
		scanner.close();
	} // end findHaikus

	/*
	 * Method: checkForHaiku --------------------------- Validates and formats a
	 * group of words to determine if they form a valid haiku. A haiku consists of
	 * three lines with 5, 7, and 5 syllables respectively.
	 * 
	 * Steps: - Iterates through the words, counting syllables and grouping words
	 * into lines. - Validates that each line meets the required syllable count. -
	 * Passes valid haikus to printHaiku for display.
	 * 
	 * Parameters: - words (ArrayList<String>): A list of words to check for a haiku
	 * structure.
	 */
	private static void checkForHaiku(ArrayList<String> words) {
		String firstLine = null;
		String secondLine = null;
		String thirdLine = null;

		int syllables = 0;
		int currentLine = 1;
		StringBuilder lineBuilder = new StringBuilder();

		for (String word : words) {
			syllables += SyllableCounter1.countSyllables(word);
			lineBuilder.append(word).append(" ");

			String thisLine = lineBuilder.toString().toLowerCase().trim();

			// Check if line consists mainly of stop-words.
			int wordCount = countWordsInLine(thisLine);

			// Check if current line is complete based on syllable count
			if (isLineComplete(currentLine, syllables)) {
				// Store the completed line in appropriate variable
				String completedLine = lineBuilder.toString().toLowerCase().trim();
				switch (currentLine) {
				case 1:
					firstLine = completedLine;
					break;
				case 2:
					secondLine = completedLine;
					break;
				case 3:
					thirdLine = completedLine;
					break;
				}

				// Reset for next line
				lineBuilder = new StringBuilder();
				syllables = 0;
				currentLine++;
			}
		}

		// Only print if we have a valid haiku (all three lines meeting the criteria)
		if (isValidHaiku(firstLine, secondLine, thirdLine)) {
			printHaiku(firstLine, secondLine, thirdLine);
		}
	} // end checkForHaiku

	/*
	 * Helper Method: isLineComplete --------------------------- Checks whether the
	 * current line of a potential haiku has reached the required syllable count.
	 * 
	 * Parameters: - lineNumber (int): The line number in the haiku (1, 2, or 3). -
	 * syllables (int): The current syllable count for the line.
	 * 
	 * Returns: - boolean: True if the line meets the syllable requirement for its
	 * position, false otherwise.
	 */
	private static boolean isLineComplete(int lineNumber, int syllables) {
		return (lineNumber == 1 && syllables == 5) || (lineNumber == 2 && syllables == 7)
				|| (lineNumber == 3 && syllables == 5);
	}

	// Helper method to check if we have a valid haiku
	private static boolean isValidHaiku(String firstLine, String secondLine, String thirdLine) {
		return firstLine != null && secondLine != null && thirdLine != null;
	}

	// Helper method to print the haiku
	private static void printHaiku(String firstLine, String secondLine, String thirdLine) {
		System.out.println(firstLine);
		System.out.println(secondLine);
		System.out.println(thirdLine);
		System.out.println("-----------------");
	}

	private static int countStopWordsInLine(String line) {
		int stopCount = 0;
		// Need to check if the line consists solely of stop-words.
		// Main stop-words we'll concern ourself with for now are, "oh" "baby" "yeah"
		// "no"

		// If line consists of mroe than 50% stop words, ditch it. <---------- PUT IN
		// ANOTHER FUNCTION

		return stopCount;
	} // end countStopWordsInLine

	private static int countWordsInLine(String line) {
		int wordCount = 0;

		return wordCount;
	} // end findLineWordCount

	private static int countSyllablesInLine(ArrayList<String> thisLine) {

		int syllablesInLine = 0;

		// Attach an iterator to the ArrayList
		Iterator<String> iterator = thisLine.iterator();

		// Loop through the words and process them
		// for (String word : words) {
		while (iterator.hasNext()) {
			String word = iterator.next();
			// Update syllable count with current word's count
			syllablesInLine += SyllableCounter1.countSyllables(word);
		}
		return syllablesInLine;

	} // end countSyllablesInLine
} // end haikuFinder
