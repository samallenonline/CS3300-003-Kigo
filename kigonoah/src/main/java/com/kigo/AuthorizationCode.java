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
    private static final String clientId = System.getenv("SPOTIFY_CLIENT_ID");
    private static final String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");
    private static final URI redirectUri = SpotifyHttpManager.makeUri(System.getenv("REDIRECT_URI"));
    
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .build();

    public static String getAccessToken(String code) {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            String accessToken = authorizationCodeCredentials.getAccessToken();
            spotifyApi.setAccessToken(accessToken);
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            return accessToken;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
        	System.err.println("Error retrieving access token: " + e.getMessage());
            e.printStackTrace(); // for further debugging
            return null;
        }
    }
}
