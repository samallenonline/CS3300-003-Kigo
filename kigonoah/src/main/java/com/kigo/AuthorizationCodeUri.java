package com.kigo;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import java.net.URI;

public class AuthorizationCodeUri {
    private static final String clientId = System.getenv("SPOTIFY_CLIENT_ID"); // Spotify Client ID retrieved from environment variables
    private static final String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET"); // Spotify Client Secret retrieved from environment variables
    private static final URI redirectUri = SpotifyHttpManager.makeUri(System.getenv("REDIRECT_URI")); // Redirect URI used during the authorization process

    // Spotify API instance configured with client details
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .build();

    // Authorization URI request, configured with the required scope and dialog settings
    private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
        .scope("user-top-read") // Request permission to read the user's top tracks and artists
        .show_dialog(true) // Ensure the user is always prompted to log in, even if already authenticated
        .build();

    /*
     * Author: Noah Kabel
     * 
     * Provides a URI for user authentication with Spotify.
     * 
     * This method generates a URI that redirects the user to Spotify's authorization page,
     * where they can log in and grant the necessary permissions for the application.
     * 
     * Parameters:
     * - None
     * 
     * Returns:
     * - A String representation of the authorization URI.
     * 
     */
    public static String getAuthorizationUri() {
        final URI uri = authorizationCodeUriRequest.execute(); // Generate the authorization URI
        System.out.println(uri.toString()); // Print the URI to the console for debugging
        return uri.toString(); // Return the URI as a string
    }
}
