package com.nokelsam.localwave;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HaikuFinder {
    public static void findHaikus(String filename) throws IOException {
    	//final String TEST_FILE = "rap-god-polished.txt";
    	//final String TEST_FILE = "joji-your-man_lryics.txt";
    	final String TEST_FILE = "romeo-blade-lyrics.txt";
    	
        File file = new File(TEST_FILE);
        Scanner scanner = new Scanner(file);
        
        ArrayList<String> currentWords = new ArrayList<>();
        int syllableCount = 0;
        
        while (scanner.hasNext()) {
            String word = scanner.next();
            currentWords.add(word);
            syllableCount += SyllableCounter1.countSyllables(word);
            
            // If we've gone over 17 syllables (5+7+5), start over
            if (syllableCount > 17) {
                currentWords.clear();
                syllableCount = 0;
                continue;
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
    }
    
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
    }

    // Helper method to check if a line is complete based on syllable count
    private static boolean isLineComplete(int lineNumber, int syllables) {
        return (lineNumber == 1 && syllables == 5) ||
               (lineNumber == 2 && syllables == 7) ||
               (lineNumber == 3 && syllables == 5);
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
    
// OLD CODE   
//    private static void checkForHaiku(ArrayList<String> words) {
//        int syllables = 0;
//        int line = 1;
//        StringBuilder currentLine = new StringBuilder();
//        
//        for (String word : words) {
//            syllables += SyllableCounter1.countSyllables(word);
//            currentLine.append(word).append(" ");
//            
//            // Check if we've completed a line
//            if ((line == 1 && syllables == 5) || (line == 2 && syllables == 12) ||  (line == 3 && syllables == 17)) {
//                
//                System.out.println(currentLine.toString().toLowerCase().trim());
//                currentLine = new StringBuilder();
//                line++;
//                
//                // Reset syllable count for next line
//                if (line <= 3) {
//                    syllables = 0;
//                }
//            }
//        }
//        System.out.println("-----------------");
//    }
    
    public static void main(String[] args) {
        try {
            findHaikus("your_lyrics_file.txt");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}