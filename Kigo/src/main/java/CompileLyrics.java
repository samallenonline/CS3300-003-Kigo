package com.kigo;

import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

public class CompileLyrics {

	public static void main(String[] args) {


        // Step 1: Get the authorization URI for the user
        String authorizationUri = AuthorizationCodeUri.getAuthorizationUri();
        System.out.println("go to link and grab code from URL, \n"
				+ "EX: http://localhost/callback?code=COPY_THIS \n"
				+ "Link: " + authorizationUri);

        // Step 2: Assume user authenticates and we get an authorization code
        Scanner kb = new Scanner(System.in);
		System.out.println("Paste code here: ");
        String authorizationCode = kb.next(); // Replace with actual authorization code

        String accessToken = AuthorizationCode.getAccessToken(authorizationCode);

		
        

        // Step 3: Fetch lyrics using the authorization code
        List<Track> tracks = UserTopTracks.getTopTracks(accessToken);
        if(tracks != null) {
        	for(Track track : tracks) {
        		StringBuilder trackArtist = new StringBuilder();
        		ArtistSimplified[] artists = track.getArtists(); 
        		for (ArtistSimplified artist : artists) { 
            		trackArtist.append(artist.getName());
        			if(artists.length > 1) {
                		trackArtist.append(", ");
        			}
        			
        		}
        		try {
        			String lyrics = LyricsOvhFetcher.getLyrics(track.getName(), trackArtist.toString());
        			if (!lyrics.equals("Lyrics not found.")) { 
        				String fileName = track.getName() + "_" + trackArtist.toString() + ".txt"; 
        				try (FileWriter writer = new FileWriter(fileName)) { 
        					writer.write(lyrics); 
        				} 
        				System.out.println("Lyrics saved to " + fileName); 
        			} else { 
        				System.out.println("Lyrics not found for " + track.getName()); 
        			}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		System.out.println();
        	}
        }else {
    		System.out.println("fuck");

        }
		    




    }

        

}
