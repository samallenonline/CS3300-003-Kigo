package com.kigo;

import java.io.FileWriter;

public class songLyricGrabber {

	public static void main(String[] args) throws Exception {
		String songName = "Disintegration";
		String artistName = "The Cure";
		String lyrics = LyricsOvhFetcher.getLyrics(songName, artistName);
		if (!lyrics.equals("Lyrics not found.")) { 
			String fileName = songName + "_" + artistName + ".txt"; 
			try (FileWriter writer = new FileWriter(fileName)) { 
				writer.write(lyrics); 
			} 
			System.out.println("Lyrics saved to " + fileName); 
		} else { 
			System.out.println("Lyrics not found for " + songName); 
		}

	}

}
