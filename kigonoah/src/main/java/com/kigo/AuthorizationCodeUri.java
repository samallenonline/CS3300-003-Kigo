package com.kigo;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import java.net.URI;

public class AuthorizationCodeUri {
    private static final String clientId = System.getenv("SPOTIFY_CLIENT_ID");
    private static final String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");
    private static final URI redirectUri = SpotifyHttpManager.makeUri(System.getenv("REDIRECT_URI"));

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .build();

    private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
        .scope("user-top-read")
        .show_dialog(true)
        .build();

    public static String getAuthorizationUri() {
        final URI uri = authorizationCodeUriRequest.execute();
        System.out.println(uri.toString()); 
        return uri.toString();
    }
}
