package com.kigo;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import java.net.URI;

public class AuthorizationCodeUri {
    private static final String clientId = "8b002ca122c7441d8f1f52b9899a8433";
    private static final String clientSecret = "4394a8e959df4767aea1b463443cd5cc";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost/callback");

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
        return uri.toString();
    }
}
