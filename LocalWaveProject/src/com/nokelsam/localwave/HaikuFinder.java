package com.nokelsam.localwave;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

public class HaikuFinder {
	
	public static void main(String[] args) throws IOException {
		
		// Check if a file name was provided as an argument.
		if (args.length != 1) {
			System.out.println("Usage: java HaikuFinder <lyrics-file.txt>");
			return;
		}

		// Get the name of the lyrics.txt file that was passed as an argument
		String lyricsFile = args[0];

		// Ensure the file has the .xtx extension
		if (!lyricsFile.endsWith(".txt")) {
			System.out.println("Error: The file must have a .txt extension.");
			return;
		}
		// For testing without having to use the command line. Comment above code out, and uncomment this line below.
		//final String LYRIC_FILE = "romeo-blade-lyrics.txt"; 	
		
		final String LYRIC_FILE = lyricsFile;
		File file = new File(LYRIC_FILE);
		Scanner scanner = new Scanner(file);

		// ArrayLists to store Strings of lyric lines
		ArrayList<String> fileLines = new ArrayList<>();

		// While lines remain
		while (scanner.hasNextLine()) {
			// Add entire line to the ArrayList.
			fileLines.add(scanner.nextLine());
		}
		scanner.close();
		
		// Call various methods to process the lyrics
		ArrayList<String> nonDuplicateLyrics = removeDuplicateLines.main(fileLines);
		ArrayList<String> polishedLyrics = processLyricLines.main(nonDuplicateLyrics);
		
		

		

		ArrayList<String> thisLineList = new ArrayList<>();
		ArrayList<String> sevenSyllableLines = new ArrayList<>();
		ArrayList<String> fiveSyllableLines = new ArrayList<>();

		int lineCount = 0;
		int fiveSyllableLineCount = 0;
		int sevenSyllableLineCount = 0;

		int numLines = polishedLyrics.size();
		String currentLine = "";
		
		// Walks through words in a file checking for presence of a haiku.
		for (int i = 0; i < numLines; i++ ) {

			// Clear the ArrayList for each new line
			thisLineList.clear();

			// Get next word in the file
			currentLine = polishedLyrics.get(i);

			// Split the current line into words by whitespace
			String[] words = currentLine.split("\\s+");

			// Add each word from the line into the an ArrayList for easy individual 
			// processing if syllable count need to be slightly altered.
			for (String word : words) {
				thisLineList.add(word);
			}

			// Count syllables
			lineCount = countSyllablesInLine(thisLineList);
			

			// If lineCount == 4
			if (lineCount == 4) {

				// Call addOneSyllable on thisLineArrayList to expand conjunctions
				thisLineList = addOneSyllable(thisLineList);
			}
			// If line matches requirements to be a haiku line,
			else if (lineCount == 5) {
				// Add to Arraylist & store for later use.
				if (!fiveSyllableLines.contains(currentLine)) {
					fiveSyllableLines.add(currentLine);
					fiveSyllableLineCount++;
				}
			} else if (lineCount == 7) {
				if (!sevenSyllableLines.contains(currentLine)) {
					sevenSyllableLines.add(currentLine);
					sevenSyllableLineCount++;
				}
			}
			// If count = 8 and the first word is "and"
			else if (lineCount == 8 && words[0].toLowerCase().equals("and")) {
				// Remove the word "and" to reach 7 syllables and increase # usable lines

				String modifiedLine = removeWordFromLine(thisLineList, "and");
				// System.out.println("Modified line: " + modifiedLine);

				// Recalculate syllables
				int newSyllableCount = countSyllablesInLine(thisLineList);
				if ((newSyllableCount == 7) && (!sevenSyllableLines.contains(modifiedLine))) {
					sevenSyllableLines.add(modifiedLine);
				}
			}
			// If count = 8 and line conatins the word "of"
			else if ((lineCount == 8) && (currentLine.contains("the"))) {
				// Remove "of" to fit 7 syllables
				String modifiedLine = removeWordFromLine(thisLineList, "the");

				// Recalculate syllables
				int newSyllableCount = countSyllablesInLine(thisLineList);
				if ((newSyllableCount == 7) && (!sevenSyllableLines.contains(modifiedLine))) {
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

	// Helper method to print the haiku
	private static void displayHaiku(Haiku aHaiku) {
		System.out.println("\n ~ Haiku ~ ");
		aHaiku.toString();
		System.out.println("-----------------");
	}

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

class Haiku {

	private String firstLine;
	private String secondLine;
	private String thirdLine;

	public Haiku(String firstLine, String secondLine, String thirdLine) {

		this.firstLine = firstLine;
		this.secondLine = secondLine;
		this.thirdLine = thirdLine;

	}

	public String getFirstLine() {
		return firstLine;
	}

	public String getSecondLine() {
		return secondLine;
	}

	public String getThirdLine() {
		return thirdLine;
	}

	@Override
	public String toString() {
		ensureCorrectPunctuation();
		return firstLine + "\n" + secondLine + "\n" + thirdLine;
	}

	// For now, only capitalize the first character in the first string
	// as anything else is a stylistic choice.
	public void ensureCorrectPunctuation() {

		char firstChar = firstLine.charAt(0);
		firstChar = Character.toUpperCase(firstChar);
		firstLine = firstChar + firstLine.substring(1);

	}
} // end Haiku class
