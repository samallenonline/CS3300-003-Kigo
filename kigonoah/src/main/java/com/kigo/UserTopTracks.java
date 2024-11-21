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
	
	private static final String clientId = System.getenv("SPOTIFY_CLIENT_ID");
    private static final String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder() 
			.setClientId(clientId) 
			.setClientSecret(clientSecret) 
			.build();

    public static List<Track> getTopTracks(String accessToken) {
    	spotifyApi.setAccessToken(accessToken);
        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks().build();
        try {
            Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            return List.of(trackPaging.getItems());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace(); // further debugging
        }
        return List.of();
    }
}
