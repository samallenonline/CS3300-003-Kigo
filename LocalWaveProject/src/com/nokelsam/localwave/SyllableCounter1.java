/*
 * Software Engineering Project
 * ---------------------------------
 * Class: SyllableCounter1
 * Purpose:
 * This class provides a static method, countSyllables, to count the number of syllables
 * in a given word. The method uses a variety of rules and heuristics, including:
 * - Identifying vowels, diphthongs (two adjacent vowels), and triphthongs (three adjacent vowels).
 * - Accounting for edge cases such as silent 'e', special suffixes, and compound words.
 * - Handling special cases like "zoo", "ion", and words with hyphens.
 * 
 * The syllable count is determined by a combination of regex-based filtering,
 * substring matching for diphthongs and triphthongs, and iterative character analysis.
 * 
 * This class is designed to assist in text processing tasks, particularly for poetry,
 * lyric analysis, and natural language processing.
 * 
 * Author: [Your Name]
 * Date: [Date]
 * Course: Software Engineering
 */
package com.nokelsam.localwave;

//import java.util.regex.Pattern;
//import java.util.regex.Matcher;

public class SyllableCounter1 {
	/*
    * Method: countSyllables
    * ---------------------------------
    * Counts the number of syllables in a given word based on a combination
    * of vowel detection, diphthong and triphthong adjustments, and special rules.
    * 
    * Parameters:
    * - word (String): The input word whose syllables are to be counted.
    * 
    * Returns:
    * - int: The calculated syllable count.
    * 
    * Key Considerations:
    * - Words with non-alphabetic characters are cleaned before processing.
    * - Handles cases like silent 'e', diphthongs, triphthongs, and specific suffixes/prefixes.
    * - Handles edge cases for words like "zoo", "ion", and hyphenated words.
    */
	public static int countSyllables(String word) {
		// Handle null or empty string cases.
		if (word == null || word.isEmpty()) {
			return 0;
		}

		// Initialize syllable count and track state of vowel detection.
		int syllableCount = 0;
		boolean isPreviousVowel = false;

		// Define constant String arrays for vowel characters, diphthongs, and triphthongs.
		final String vowels = "aeiouy";
		final String[] diphthongs = { "ai", "ua", "au", "ae", "ay", "ia", "ea", "ee", "ei", "eo", "oa", "oo", "ou", "ie", "ue", "oi", "oy", "io", "iou",				  };
		final String[] triphthongs = { "iou", "eau", "eae", "eie" };
		final int diphthongsLength = diphthongs.length;
		final int triphthongsLength = triphthongs.length;

		// Variables for diphthong and triphthong detection.
		boolean containsDiphthong = false;
		boolean containsTriphthong = false;
		
		// Counters to track multiple diphs/triphthongs per word.
		int diphCount = 0;
		int triphCount = 0;
		
		boolean isVowel = false;
		// boolean isPreviousConsonant = false;

		// Convert word to lowercase and remove non-alphabetic characters.
		word = word.toLowerCase().trim();
		// Get rid of anything that isn't a letter using regex.
		word = word.replaceAll("[^a-zA-Z]", "");

		// Loop through string array checking if word contains diphthong(s).
		for (int j = 0; j < diphthongsLength; j++) {
			String thisDip = diphthongs[j];
			if (word.contains(thisDip)) {
				containsDiphthong = true;
				//diphCount++;
			}
		} // end diphthong check
		// Loop through string array checking if word contains triphthong(s).
		for (int j = 0; j < triphthongsLength; j++) {
			String thisTrip = triphthongs[j];
			if (word.contains(thisTrip)) {
				containsTriphthong = true;
				//triphCount++;
			}
		} // end diphthong check

		if (word.length() > 2 && word.startsWith("zoo")) {
			syllableCount++;
		}
		// Begin counting syllables in word by counting the number of vowels.
		// This is done by comparing to string of vowels.
		for (int i = 0; i < word.length(); i++) {

			// Boolean variables: isVowel - to store whether or not each letter is a vowel
			// isDiphthong - whether or not word contains a dipthong. If so, don't double
			// count syllables based on vowels.
			isVowel = vowels.indexOf(word.charAt(i)) != -1;

			if (isVowel) {
				syllableCount++;
			}
			// Set isPreviousVowel to the value of isVowel that way if current letter is a
			// vowel, it will be counted on the next iteration.
			isPreviousVowel = isVowel;
			
		} // for char in word
		
		// Loop through string array checking if word contains triphthongs and count them.
		for (String thisTrip : triphthongs) {
		    int index = word.indexOf(thisTrip);
		    while (index != -1) {
		        triphCount++;
		        word = word.substring(0, index) + word.substring(index + thisTrip.length());  
		        index = word.indexOf(thisTrip);
		    }
		}
		// Loop through string array checking if word contains diphthongs and count them.
		for (String thisDip : diphthongs) {
		    int index = word.indexOf(thisDip);
		    while (index != -1) {
		        diphCount++;
		        word = word.substring(0, index) + word.substring(index + thisDip.length());  
		        index = word.indexOf(thisDip);
		    }
		}
		
		// If the word contains a triphthong (3 adjacent vowels), decrement syllable
		// count by 2.
		if (containsTriphthong) {
			syllableCount = syllableCount - 2;
		}
		// If the word contains a diphthong (2 adjacent vowels), decrement syllable
		// count by 1.
		else if (containsDiphthong) {
			syllableCount = syllableCount - 1 *diphCount;
		} else {
			System.out.print("");
		}


		// ##################################### KINDA WONKY BUT WORKS ####################################### \\
		// Checks for a silent "e" at the end of word. Note: This check is happening
		// after initial vowel count, so the syllableCount should be decremented.
		// Example: "make" or "ale" wwill have a count of 2 syllables, but it's actually 1
		// But for words shorter than 3 characters, like "be" or "me," the count should
		// not be decremented. Also handles other edge cases. Ex. "Yes"=1
		if ( (word.length() > 2 && word.endsWith("e") ) && !word.endsWith("le") && (word.compareTo("the") != 0) ) {
			syllableCount--;
		}
		if (word.contains("-")) {
			syllableCount++;
		}
		// Case: "zooplankton" or "zoophobia"
		if (word.startsWith("zoo") && !word.endsWith("zoo")) {
			syllableCount++;
		}
		if ( word.endsWith("ia") || word.endsWith("ias")) {
			syllableCount++;
		}
		

		// Check if the word is longer than 3 characters and the word contains "ved,"
		// if so, decrement the syllable count. Ex. "caved" is 1 syllable
//		if ( (word.length() > 2 && word.endsWith("ved")) || (word.endsWith("wed")) || (word.endsWith("red"))) {
//
//			syllableCount--;
//		}
		// 
		if (word.length() > 2 && word.endsWith("ed")) {
			syllableCount--;
		}
		if ((word.startsWith("y")) || word.endsWith("ion") || word.endsWith("ile") || 
				word.endsWith("iles") || word.endsWith("ole") || word.startsWith("aa")) {
			syllableCount--;
		}
		if ( word.endsWith("nes")) {
			syllableCount--;
		}
		if (word.contains("some")) {
			syllableCount--;
		}
		return syllableCount;
	} // end main
} // end syllableCounter
