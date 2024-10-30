/* This program is designed to test the syllableCounter1 function by reading 
 * from a file of lyrics and counting the syllables of each word in a while loop.
 * 
 * 
 */
package com.nokelsam.localwave;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class testingSyllableCounter {

	public static void main(String[] args) throws IOException {

		// Set up test files and scanner to read from file.
		//final String TEST_FILE = "joji-your-man_lryics.txt";
		//final String TEST_FILE = "rap-god-polished.txt";
		//final String TEST_FILE = "1000_words_source.txt";
		final String TEST_FILE = "romeo-blade-lyrics.txt";

		File testFile = new File(TEST_FILE);
		Scanner fileReader = new Scanner(testFile);
		
		final String OUT_FILE = "outputFile.txt";
		FileWriter writer = new FileWriter(OUT_FILE);

		// String & int to store each word and syllable count.
		String word = "";
		int syllables = 0;

		int totalSyllables = 0;

		// Reads through each word in file, processing each word for syllables.
		while (fileReader.hasNext()) {

			word = fileReader.next();
			syllables = SyllableCounter1.countSyllables(word);
			System.out.println(syllables + " " + word);

			 writer.write(syllables + " " + word + "\n");
			
			totalSyllables += syllables;
		} // end while

		System.out.println("\n Total Syllables Counted in file: " + totalSyllables);
		writer.flush();
		writer.close();
		fileReader.close();
		System.out.println("\n");
		compareResultsByFiles();
		
	} // end main
	
	/*
	 * Method compares words line by line to a file containing the exact same list
	 * of words where each word is preceeded by its correct syllable count.
	 */
	public static void compareResultsByFiles() throws IOException {
		// Different files to test program on.
		//final String COMPARE_FILE = "rap-god-syllables.txt";
	    final String OUTPUT_FILE = "outputFile.txt";
	    //final String COMPARE_FILE = "1000-correct-syllables.txt";
	    final String COMPARE_FILE = "romeo-blade-syllables.txt";
	    
	    
	    File compareFile = new File(COMPARE_FILE);
	    File outputFile = new File(OUTPUT_FILE);
	    
	    Scanner readAnswers = new Scanner(compareFile);
	    Scanner readOutput = new Scanner(outputFile);
		
        int lineNumber = 0;
        int matchCount = 0;
        int totalLines = 0;

        // Compare files line by line
        while (readAnswers.hasNext() && readOutput.hasNext() /*&& lineNumber < 200*/) {
            lineNumber++;
            
            // Read syllable count and word from both files
            int correctSyllables = readAnswers.nextInt();
            String word = readAnswers.next();
            
            int outputSyllables = readOutput.nextInt();
            String outputWord = readOutput.next();
            
            // Compare both syllable count and word
            if (correctSyllables == outputSyllables && word.equals(outputWord)) {
                matchCount++;
            } else {
                System.out.printf("Mismatch at line %d", lineNumber);
                System.out.printf("\nWord :%s\n", word);
                System.out.printf("Expected: %d\n", correctSyllables);
                System.out.printf("Found   : %d\n", outputSyllables);
                System.out.println();
            }
            
            totalLines++;
        }
        
        // Print summary statistics
        System.out.println("\nComparison Summary-");
        System.out.printf("Total words compared: %d%n", totalLines);
        System.out.printf("Matching words: %d%n", matchCount);
        System.out.printf("Accuracy: %.2f%%%n", (matchCount * 100.0) / totalLines);
		
		
	} // end compareResults
	

} // end testingSyllableCounter
