package com.kigo;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;
import java.io.IOException;
import java.net.URI;

public class AuthorizationCode {
    private static final String clientId = System.getenv("SPOTIFY_CLIENT_ID"); // Spotify Client ID retrieved from environment variables
    private static final String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET"); // Spotify Client Secret retrieved from environment variables
    private static final URI redirectUri = SpotifyHttpManager.makeUri(System.getenv("REDIRECT_URI")); // Redirect URI used during authorization process

    // Spotify API instance configured with client details
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .build();

    /*
     * Author Noah Kabel
     * 
     * Retrieves an access token using the provided authorization code.
     * 
     * Parameters:
     * - String code: The authorization code obtained from the Spotify authorization URL after user login.
     * 
     * Returns:
     * - A String containing the access token if successful, or null in case of an error.
     * 
     */
    public static String getAccessToken(String code) {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build(); // Build the authorization code request
        try {
            // Execute the request and retrieve the credentials
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            String accessToken = authorizationCodeCredentials.getAccessToken(); // Extract the access token
            spotifyApi.setAccessToken(accessToken); // Set the access token for future API calls
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken()); // Set the refresh token for token renewal
            return accessToken; // Return the access token
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.err.println("Error retrieving access token: " + e.getMessage()); // Log the error message
            e.printStackTrace(); // Print the stack trace for debugging
            return null; // Return null in case of an error
        }
    }
}
