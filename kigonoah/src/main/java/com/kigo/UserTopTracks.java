package com.kigo;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.List;

public class UserTopTracks {

    private static final String clientId = System.getenv("SPOTIFY_CLIENT_ID"); // Spotify Client ID from environment variables
    private static final String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET"); // Spotify Client Secret from environment variables

    // Spotify API instance configured with client details
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .build();

    /*
     * Author: Noah Kabel
     * 
     * Retrieves the user's top tracks from Spotify.
     * 
     * This method uses the Spotify Web API to fetch the top tracks that the user has been listening to,
     * based on their listening history and preferences.
     * 
     * Parameters:
     * - String accessToken: The OAuth access token required to authenticate the API request.
     * 
     * Returns:
     * - A List of Track objects representing the user's top tracks. 
     *   If an error occurs during the request, an empty list is returned.
     * 
     */
    public static List<Track> getTopTracks(String accessToken) {
        spotifyApi.setAccessToken(accessToken); // Set the access token for the API instance
        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks().build(); // Build the request to fetch top tracks
        try {
            Paging<Track> trackPaging = getUsersTopTracksRequest.execute(); // Execute the request and retrieve the paging object
            return List.of(trackPaging.getItems()); // Return the list of tracks
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage()); // Log the error message
            e.printStackTrace(); // Print the stack trace for debugging
        }
        return List.of(); // Return an empty list in case of an error
    }
}