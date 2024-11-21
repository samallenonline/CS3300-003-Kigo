package com.kigo;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


public class ClientCredentialsEX {

	private static final String clientId = "8b002ca122c7441d8f1f52b9899a8433";
	  private static final String clientSecret = "4394a8e959df4767aea1b463443cd5cc";

	  private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
	    .setClientId(clientId)
	    .setClientSecret(clientSecret)
	    .build();
	  private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
	    .build();

	  public static void clientCredentials_Sync() {
	    try {
	      final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

	      // Set access token for further "spotifyApi" object usage
	      spotifyApi.setAccessToken(clientCredentials.getAccessToken());

	      System.out.println("Expires in: " + clientCredentials.getExpiresIn());
	    } catch (IOException | SpotifyWebApiException | ParseException e) {
	      System.out.println("Error: " + e.getMessage());
	    }
	  }

	  public static void clientCredentials_Async() {
	    try {
	      final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();

	      // Thread free to do other tasks...

	      // Example Only. Never block in production code.
	      final ClientCredentials clientCredentials = clientCredentialsFuture.join();

	      // Set access token for further "spotifyApi" object usage
	      spotifyApi.setAccessToken(clientCredentials.getAccessToken());

	      System.out.println("Expires in: " + clientCredentials.getExpiresIn());
	    } catch (CompletionException e) {
	      System.out.println("Error: " + e.getCause().getMessage());
	    } catch (CancellationException e) {
	      System.out.println("Async operation cancelled.");
	    }
	  }
	  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		clientCredentials_Sync();
	    clientCredentials_Async();
	}

}
