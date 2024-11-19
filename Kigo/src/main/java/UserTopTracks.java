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
	
	private static final String clientId = "8b002ca122c7441d8f1f52b9899a8433";
    private static final String clientSecret = "4394a8e959df4767aea1b463443cd5cc";

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
        }
        return List.of();
    }
    

}
