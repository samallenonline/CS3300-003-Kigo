package com.nokelsam.localwave;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class processLyricLines {

	public static void main(String[] args) throws IOException {
		
		final String TEST_FILE = "blinding-lights-lyrics.txt";

		File file = new File(TEST_FILE);
		Scanner scanner = new Scanner(file);

		ArrayList<String> thisLineArrayList = new ArrayList<>();

		
		scanner.close();
	} // end main

} // end processLyricLines class
