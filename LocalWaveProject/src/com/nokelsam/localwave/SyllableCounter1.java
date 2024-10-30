
package com.nokelsam.localwave;

//import java.util.regex.Pattern;
//import java.util.regex.Matcher;

public class SyllableCounter1 {

	public static int countSyllables(String word) {
		// Check if the word is empty/null.
		if (word == null || word.isEmpty()) {
			return 0;
		}

		int syllableCount = 0;
		boolean isPreviousVowel = false;

		// Strings to check for the presence of vowels, diphthongs, & triphthongs.
		final String vowels = "aeiouy";

		final String[] diphthongs = { "ai", "ua", "au", "ae", "ay", "ia", "ea", "ee", "ei", "eo", "oa", "oo", "ou", "ie", "ue", "oi", "oy", "io", "iou",
				  };
		final String[] triphthongs = { "iou", "eau", "eae", "eie" };

		final int diphthongsLength = diphthongs.length;
		final int triphthongsLength = triphthongs.length;

		boolean containsDiphthong = false;
		boolean containsTriphthong = false;
		
		// Word may contain multiple diphs/triphthongs
		int diphCount = 0;
		int triphCount = 0;
		
		boolean isVowel = false;
		// boolean isPreviousConsonant = false;

		// Convert the string to lowercase for comparisons.
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


		// ##################################### KINDA WONKY BUT WORKS #######################################\\
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
		
		/////////////


		// Check if the word is longer than 3 characters and the word contains "ved,"
		// if so, decrement the syllable count. Ex. "caved" is 1 syllable
//		if ( (word.length() > 2 && word.endsWith("ved")) || (word.endsWith("wed")) || (word.endsWith("red"))) {
//
//			syllableCount--;
//		}
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

		return syllableCount;
	} // end main
} // end syllableCounter
