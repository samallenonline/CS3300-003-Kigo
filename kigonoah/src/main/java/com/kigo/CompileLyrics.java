// noah's code, modified by sam to ensure that the process of retrieving the URl and authorization code is automated- 
// i tried to preserve as much of the original logic as possible.  
package com.kigo;

import java.io.FileWriter;
import java.util.List;

import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

public class CompileLyrics {

    // define where lyric files will be outputted
    private static final String LYRICS_FOLDER = "lyrics/";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("  java -jar Kigo-0.0.1-SNAPSHOT.jar login");
            System.out.println("  java -jar Kigo-0.0.1-SNAPSHOT.jar callback <authorization_code>");
            System.out.println("  java -jar Kigo-0.0.1-SNAPSHOT.jar lyrics <access_token>");
            return;
        }

        String command = args[0];

        switch (command) {
            case "login":
                // Step 1: Get the authorization URI for the user
                String authorizationUri = AuthorizationCodeUri.getAuthorizationUri();
                System.out.println(authorizationUri); // output the URL for Node.js to capture
                break;

            case "callback":
                if (args.length < 2) {
                    System.err.println("Error: Missing authorization code.");
                    return;
                }
                // Step 2: Retrieve the access token
                String authorizationCode = args[1];
                String accessToken = AuthorizationCode.getAccessToken(authorizationCode);
                System.out.println(accessToken); // output the token for Node.js to capture
                break;

            case "lyrics":
                if (args.length < 2) {
                    System.err.println("Error: Missing access token.");
                    return;
                }
                String accessTokenForLyrics = args[1];
                fetchLyrics(accessTokenForLyrics);
                break;

            default:
                System.out.println("Unknown command: " + command);
                System.out.println("Available commands: login, callback, lyrics");
        }
    }

    private static void fetchLyrics(String accessToken) {
        // Step 3: Fetch lyrics using the access token
        List<Track> tracks = UserTopTracks.getTopTracks(accessToken);
        if (tracks != null) {
            for (Track track : tracks) {
                StringBuilder trackArtist = new StringBuilder();
                ArtistSimplified[] artists = track.getArtists();
                for (ArtistSimplified artist : artists) {
                    trackArtist.append(artist.getName());
                    if (artists.length > 1) {
                        trackArtist.append(", ");
                    }
                }
                try {
                    String lyrics = LyricsOvhFetcher.getLyrics(track.getName(), trackArtist.toString());
                    if (!lyrics.equals("Lyrics not found.")) {
                        String fileName = LYRICS_FOLDER + track.getName() + "_" + trackArtist.toString() + ".txt";
                        try (FileWriter writer = new FileWriter(fileName)) {
                            writer.write(lyrics);
                        }
                        System.out.println("Lyrics saved to " + fileName);
                    } else {
                        System.out.println("Lyrics not found for " + track.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Error fetching top tracks.");
        }
    }
}

