package com.nokelsam.localwave;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class SpotifyTrackInfo {
	
	public static void main(String[] args) throws IOException, URISyntaxException {
        // Access token you received earlier
        String accessToken = "your_access_token";

        // The track ID you want to retrieve information about
        String trackId = "3n3Ppam7vgaVa1iaRUc9Lp"; // Example: "Enter Sandman" by Metallica

        // Construct the URI and then convert it to a URL
        URI uri = new URI("https", "api.spotify.com", "/v1/tracks/" + trackId, null);
        URL url = uri.toURL();

        // Set up the connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");

        // Get the response
        int responseCode = conn.getResponseCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        // Print the response (track information)
        if (responseCode == 200) {
            System.out.println("Response: " + response.toString());
        } else {
            System.out.println("Error: " + responseCode);
            System.out.println("Response: " + response.toString());
        }
    }
}
