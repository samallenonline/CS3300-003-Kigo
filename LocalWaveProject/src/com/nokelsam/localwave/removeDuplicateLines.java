package com.nokelsam.localwave;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class removeDuplicateLines {

	public static ArrayList<String> main(ArrayList<String> lines) {

		ArrayList<String> processedLyricsList = new ArrayList<>();

		// Call sort to make finding repeats easier.
		Collections.sort(processedLyricsList);

		// Remove duplicate lines.
		ArrayList<String> nonDuplicateLyrics = new ArrayList<>();

		// Check if the list is empty
		if (!lines.isEmpty()) {
			nonDuplicateLyrics = removeDuplicateLinesFromArrayList(lines);

		}  else {
			///////-------- ADD CODE TO Send to error log file -------------//////
			System.out.println("The list is empty.");
		}

		// Return the list even if empty.
		return nonDuplicateLyrics;
	} // end main

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
	} // end printArrayList<String>

	public static ArrayList<String> removeDuplicateLinesFromArrayList(ArrayList<String> list) {

		ArrayList<String> nonDuplicatesList = new ArrayList<>();

		// Create iterator to move through lines in ArrayList
		Iterator<String> iterator = list.iterator();

		String lyric = "";

		while (iterator.hasNext()) {
			lyric = iterator.next().toString();
			// If not already in the nonDuploicates list
			if (!nonDuplicatesList.contains(lyric)) {
				nonDuplicatesList.add(lyric);
			}
		} // end while

		return nonDuplicatesList;
	} // end removeDuplicateLinesFromArrayList

} // end processLyricLines class
