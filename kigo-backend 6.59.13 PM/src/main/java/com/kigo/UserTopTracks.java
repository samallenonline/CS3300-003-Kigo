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

	private static final String accessToken = "BQBcU95FXqGKhaQD24OZrKFl_mhUSeQ7y352r7F8VeMI74_LDCHxAN01oSTnmaDDEHUAiUZ1Au3Xm_nUNidMz0a7ZL_3ar9YKBvuUU71OtBiRIlzjn_EJYv-RMgPzY_f6yQbljpKy1Gd8_BW7kfUS8Xnj3AkifFB2wk3haaK9uVqXqvMr9t4PhiftLLe6tRsO45vNBVLrzhJGpwRLNeaJA";
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();

    public static List<Track> getTopTracks() {
        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks().build();
        try {
            Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            return List.of(trackPaging.getItems());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return List.of();
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AuthorizationCode.authorizationCode_Sync();
		List<Track> topTracks = getTopTracks();
        topTracks.forEach(track -> System.out.println(track.getName() + " by " + track.getArtists()[0].getName()));
	}

}
