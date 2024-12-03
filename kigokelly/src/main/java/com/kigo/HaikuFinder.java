// kelly's work - modified by sam to ensure that every file in /lyrics is processed and haikus are generated and stored in /haikus 
package com.kigo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import com.kigo.processLyricLines;
import com.kigo.removeDuplicateLines;

import java.util.Iterator;
import java.io.FileWriter;

public class HaikuFinder {

	public static void main(String[] args) throws IOException {

		// Check if a file name was provided as an argument.
		// if (args.length != 1) {
		//	System.out.println("Usage: java HaikuFinder <lyrics-file.txt>");
		//	return;
		// }

		// Get the name of the lyrics.txt file that was passed as an argument
		//String lyricsFile = args[0];

		// Ensure the file has the .xtx extension
		//if (!lyricsFile.endsWith(".txt")) {
		//	System.out.println("Error: The file must have a .txt extension.");
		//	return;
		// }
		// For testing without having to use the command line. Comment above code out, and uncomment this line below.		
		
	    // define input and output directories
		final String LYRICS_FOLDER = "../kigonoah/lyrics";
        final String HAIKUS_FOLDER = "../kigokelly/haikus";

        // ensure output directory exists
        File haikusDir = new File(HAIKUS_FOLDER);
        if (!haikusDir.exists()) {
            haikusDir.mkdirs();
        }

        //get all text files in the /lyrics folder
        File lyricsDir = new File(LYRICS_FOLDER);
        File[] lyricFiles = lyricsDir.listFiles((dir, name) -> name.endsWith(".txt"));

        if (lyricFiles == null || lyricFiles.length == 0) {
            System.out.println("No lyric files found in the directory: " + LYRICS_FOLDER);
            return;
        }

        // process each file
        for (File lyricFile : lyricFiles) {
        	
        	File file = lyricFile;	
			Scanner scanner = new Scanner(file);
			
			// Grab the first two lines storing Title and Artist for printing signature
			String title = scanner.nextLine();
			String artist = scanner.nextLine();
	
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
			ArrayList<String> fourSyllableLines = new ArrayList<>();
			ArrayList<String> fiveSyllableLines = new ArrayList<>();
			ArrayList<String> sixSyllableLines = new ArrayList<>();
			ArrayList<String> sevenSyllableLines = new ArrayList<>();
			ArrayList<String> eightSyllableLines = new ArrayList<>();
			ArrayList<String> otherCountLines = new ArrayList<>();

			int lineSyllableCount = 0;

			int numLines = polishedLyrics.size();
			String currentLine = "";

			// Walks through words in a file checking for presence of a haiku.
			for (int i = 0; i < numLines; i++) {

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
				lineSyllableCount = countSyllablesInLine(thisLineList);

				////////////// COUNT NEAR SYLLABLE MARK LINES AND MOVE INTO RESPECTIVE LISTS
				////////////// /////////////
				if (lineSyllableCount == 4) {
					fourSyllableLines.add(currentLine);
					// Call addOneSyllable to try to expand conjunctions
					// thisLineList = addOneSyllable(thisLineList);
				}
				// If line matches requirements to be a haiku line,
				else if (lineSyllableCount == 5) {
					// Add to Arraylist & store for later use.
					fiveSyllableLines.add(currentLine);
				} else if (lineSyllableCount == 6) {
					sixSyllableLines.add(currentLine);
				} else if (lineSyllableCount == 7) {
					sevenSyllableLines.add(currentLine);
				} else if (lineSyllableCount == 8) {
					eightSyllableLines.add(currentLine);
				} else {
					if (lineSyllableCount >= 9) {
						otherCountLines.add(currentLine);
					}
				}

				// If count = 8 and the first word is "and"
				if (lineSyllableCount == 8 && words[0].toLowerCase().equals("and")) {
					// Remove the word "and" to reach 7 syllables and increase # usable lines

					String modifiedLine = removeWordFromLine(thisLineList, "and");

					// Recalculate syllables
					int newSyllableCount = countSyllablesInLine(thisLineList);
					if ((newSyllableCount == 7) && (!sevenSyllableLines.contains(modifiedLine))) {
						sevenSyllableLines.add(modifiedLine);
					}
				}
				// If count = 8 and line conatins the word "of"
				else if ((lineSyllableCount == 8) && (currentLine.contains("the"))) {
					// Remove "the" to fit 7 syllables
					String modifiedLine = removeWordFromLine(thisLineList, "the");

					// Recalculate syllables
					int newSyllableCount = countSyllablesInLine(thisLineList);
					if ((newSyllableCount == 7) && (!sevenSyllableLines.contains(modifiedLine))) {
						sevenSyllableLines.add(modifiedLine);
					}
				} // end if else-if
			} // end while

			// Check how many 4, 5, 6, and 7 syllable lines we have to work with
			int numFourSyllableLines = fourSyllableLines.size();
			int numFiveSyllableLines = fiveSyllableLines.size();
			int numSixSyllableLines = sixSyllableLines.size();
			int numSevenSyllableLines = sevenSyllableLines.size();

			System.out.println("\n");
			System.out.println("Contents of 4 Syllable ArrayList:");
			printArrayList(fourSyllableLines);
			System.out.println("\n");
			System.out.println("Contents of 5 Syllable ArrayList:");
			printArrayList(fiveSyllableLines);
			System.out.println("");
			System.out.println("Contents of 6 Syllable ArrayList:");
			printArrayList(sixSyllableLines);
			System.out.println("");
			System.out.println("Contents of 7 Syllable ArrayList:");
			printArrayList(sevenSyllableLines);

			splitLinesByCommas(otherCountLines, fourSyllableLines, fiveSyllableLines, sixSyllableLines, sevenSyllableLines);

			boolean foundMoreFiveLines = false;
			// If 5-Syll lines <= 1, try to make more from fourSyllableLines
			//if (numFourSyllableLines >= 1 && numFiveSyllableLines <= 1) {
				foundMoreFiveLines = makeFiveSyllableLinesFromFour(fourSyllableLines, fiveSyllableLines);
			//}
			//if (!foundMoreFiveLines) {
				makeFiveSyllableLinesFromSix(fiveSyllableLines, sixSyllableLines);
			//}
	
            // generate haikus
            StringBuilder haikuContent = new StringBuilder();
            for (int i = 0; i < 3; i++) { // generate three haikus
                String[] haiku = putRandomHaikuTogetherFromArrayLists(fiveSyllableLines, sevenSyllableLines);
                haikuContent.append(String.join("\n", haiku)).append("\n\n");
            }

            // save haikus to file in /haikus folder
            String haikuFileName = HAIKUS_FOLDER + "/" + lyricFile.getName().replace(".txt", "_haikus.txt");
            try (FileWriter writer = new FileWriter(haikuFileName)) {
                writer.write(haikuContent.toString() + "\n");
                writer.write("Lyrics - " + artist);
                
            }
	
			
		} // end main
	} // end 
	
	private static void splitLinesByCommas(ArrayList<String> otherLines, ArrayList<String> fourLines,
			ArrayList<String> fiveLines, ArrayList<String> sixLines, ArrayList<String> sevenLines) {
		// ArrayList<String> result = new ArrayList<>();

		for (String line : otherLines) {
			// Split the line by commas
			String[] subLines = line.split(",");

			for (String subLine : subLines) {
				subLine = subLine.trim(); // Remove leading/trailing spaces

				// Count syllables in the sub-line
				int syllableCount = countSyllablesInLine(subLine);

				// Check if the syllable count matches the desired range
				if (syllableCount == 4) {
					fourLines.add(subLine);
				} else if (syllableCount == 5) {
					fiveLines.add(subLine);
				} else if (syllableCount == 6) {
					sixLines.add(subLine);
				} else if (syllableCount == 5) {
					sevenLines.add(subLine);
				}
			}
		}

		// return result;
	}
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

	private static String expandContraction(String word) {
		// Define a map for expanding contractions to increase syllables
		Map<String, String[]> expansionsMap = new HashMap<>();

		expansionsMap.put("i'm", new String[] { "I", "am" });
		expansionsMap.put("you're", new String[] { "you", "are" });
		expansionsMap.put("we're", new String[] { "we", "are" });
		expansionsMap.put("they're", new String[] { "they", "are" });
		expansionsMap.put("he's", new String[] { "he", "is" });
		expansionsMap.put("she's", new String[] { "she", "is" });
		expansionsMap.put("it's", new String[] { "it", "is" });
		expansionsMap.put("i've", new String[] { "I", "have" });
		expansionsMap.put("you've", new String[] { "you", "have" });
		expansionsMap.put("we've", new String[] { "we", "have" });
		expansionsMap.put("they've", new String[] { "they", "have" });
		expansionsMap.put("don't", new String[] { "do", "not" });
		expansionsMap.put("doesn't", new String[] { "does", "not" });
		expansionsMap.put("didn't", new String[] { "did", "not" });
		expansionsMap.put("i'll", new String[] { "I", "will" });
		expansionsMap.put("you'll", new String[] { "you", "will" });
		expansionsMap.put("we'll", new String[] { "we", "will" });
		expansionsMap.put("they'll", new String[] { "they", "will" });
		expansionsMap.put("i'd", new String[] { "I", "would" });
		expansionsMap.put("you'd", new String[] { "you", "would" });
		expansionsMap.put("he'd", new String[] { "he", "would" });
		expansionsMap.put("she'd", new String[] { "she", "would" });
		expansionsMap.put("they'd", new String[] { "they", "would" });
		expansionsMap.put("gonna", new String[] { "going", "to" });
		expansionsMap.put("wanna", new String[] { "want", "to" });
		expansionsMap.put("lemme", new String[] { "let", "me" });
		expansionsMap.put("gimme", new String[] { "give", "me" });
		expansionsMap.put("y'all", new String[] { "you", "all" });

		// Convert input to lowercase for case-insensitive matching
		String lowerCaseWord = word.toLowerCase();

		// Check if the word exists in the expansions map
		if (expansionsMap.containsKey(lowerCaseWord)) {
			// Join the expanded parts into a single string
			return String.join(" ", expansionsMap.get(lowerCaseWord));
		}

		// If no expansion is found, return the original word
		return word;

	} // end tryToAddOneSyllable

	private static boolean makeFiveSyllableLinesFromSix(ArrayList<String> fiveList, ArrayList<String> sixList) {

		boolean addedOne = false;

		// For every 4-syllable line in the array list
		for (int i = 0; i < sixList.size(); i++) {

			// Pull out each line as a string
			String line = sixList.get(i);
			// Convert it to a string array
			String[] words = line.split("\\s+");

			StringBuilder updatedLine = new StringBuilder();
			boolean lineUpdated = false;

			// Check each word in the line
			for (String word : words) {
				// Try to expand contractions
				String updated = makeContraction(word);
				if (!word.equals(updated)) {
					lineUpdated = true;
				}
				// Add the (possibly updated) word to the new line
				updatedLine.append(updated).append(" ");
			}

			// If at least one word was updated, add the new line to fiveList
			if (lineUpdated) {
				fiveList.add(updatedLine.toString().trim());
				addedOne = true;
			}
		} // end outer loop

		return addedOne;
	} // end makeFiveSyllableLinesFromFour

	private static boolean makeFiveSyllableLinesFromFour(ArrayList<String> fourList, ArrayList<String> fiveList) {

		boolean addedOne = false;

		// For every 4-syllable line in the array list
		for (int i = 0; i < fourList.size(); i++) {

			// Pull out each line as a string
			String line = fourList.get(i);
			// Convert it to a string array
			String[] words = line.split("\\s+");

			StringBuilder updatedLine = new StringBuilder();
			boolean lineUpdated = false;

			// Check each word in the line
			for (String word : words) {
				// Try to expand contractions
				String updated = expandContraction(word);
				if (!word.equals(updated)) {
					lineUpdated = true;
				}
				// Add the (possibly updated) word to the new line
				updatedLine.append(updated).append(" ");
			}

			// If at least one word was updated, add the new line to fiveList
			if (lineUpdated) {
				fiveList.add(updatedLine.toString().trim());
				addedOne = true;
			}
		} // end outer loop

		return addedOne;
	} // end makeFiveSyllableLinesFromFour

	private static String makeContraction(String word) {

		// Define a map for collapsing phrases to reduce syllables
		Map<String, String> contractionsMap = new HashMap<>();

		contractionsMap.put("i am", "I'm");
		contractionsMap.put("you are", "you're");
		contractionsMap.put("we are", "we're");
		contractionsMap.put("they are", "they're");
		contractionsMap.put("he is", "he's");
		contractionsMap.put("she is", "she's");
		contractionsMap.put("do not", "don't");
		contractionsMap.put("does not", "doesn't");
		contractionsMap.put("did not", "didn't");
		contractionsMap.put("i will", "I'll");
		contractionsMap.put("you will", "you'll");
		contractionsMap.put("we will", "we'll");
		contractionsMap.put("they will", "they'll");
		contractionsMap.put("he will", "he'll");
		contractionsMap.put("she will", "she'll");
		contractionsMap.put("i would", "I'd");
		contractionsMap.put("you would", "you'd");
		contractionsMap.put("he would", "he'd");
		contractionsMap.put("she would", "she'd");
		contractionsMap.put("they would", "they'd");

		// Convert input to lowercase for case-insensitive matching
		String lowerCaseWord = word.toLowerCase();

		// Check if the word exists in the expansions map
		if (contractionsMap.containsKey(lowerCaseWord)) {
			// Join the expanded parts into a single string
			return String.join(" ", contractionsMap.get(lowerCaseWord));
		}

		// If no expansion is found, return the original word
		return word;
	}

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

	private static int countSyllablesInLine(String line) {

		int syllablesInLine = 0;

		// Split the line into words
		String[] words = line.split("\\s+");

		// Loop through the words and process them
		for (String word : words) {
			// Update syllable count with current word's count
			syllablesInLine += SyllableCounter1.countSyllables(word);
		}
		return syllablesInLine;

	} // end countSyllablesInLine

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
