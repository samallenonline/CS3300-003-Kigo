package com.nokelsam.localwave;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class removeDuplicateLines {

	public static void main(String[] args) throws IOException {

		final String TEST_FILE = "blinding-lights-lyrics.txt";

		File file = new File(TEST_FILE);
		Scanner scanner = new Scanner(file);

		ArrayList<String> fileLyricLinesList = new ArrayList<>();

		// While lines remain
		while (scanner.hasNextLine()) {
			// Add entire line to the ArrayList.
			fileLyricLinesList.add(scanner.nextLine());
		}

		// Call sort to make finding repeats easier.
		Collections.sort(fileLyricLinesList);

		// Remove duplicate lines.
		ArrayList<String> nonDuplicateLyricList = removeDuplicateLinesFromArrayList(fileLyricLinesList);

		printArrayList(nonDuplicateLyricList);

		// Output file
		final String LYRICS_OUT = "non-duplicate-lyrics.txt";
		FileWriter outWriter = new FileWriter(LYRICS_OUT);

		// Check if the list is empty
		if (nonDuplicateLyricList.isEmpty()) {
			System.out.println("The list is empty.");

		} else {
			// Print each string in the list
			for (String item : nonDuplicateLyricList) {
				outWriter.write(item + "\n");
			}
		}

		scanner.close();
		outWriter.close();
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

	public static void printArrayListToFile(ArrayList<String> list) {

	} // end printArrayList<String>

	public static ArrayList<String> removeDuplicateLinesFromArrayList(ArrayList<String> list) {

		ArrayList<String> nonDuplicateLyricList = new ArrayList<>();

		// Create iterator to move through lines in ArrayList
		Iterator<String> iterator = list.iterator();

		String lyric = "";

		while (iterator.hasNext()) {
			lyric = iterator.next().toString();
			// If not already in the nonDuploicates list
			if (!nonDuplicateLyricList.contains(lyric)) {
				nonDuplicateLyricList.add(lyric);
			}
		} // end while

		return nonDuplicateLyricList;
	} // end removeDuplicateLinesFromArrayList

} // end processLyricLines class
