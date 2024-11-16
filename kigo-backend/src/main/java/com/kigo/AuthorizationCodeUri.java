package com.kigo;


import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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
	    //.state("x4xkmn9pu3j6ukrs8n")
	    .scope("user-top-read")
	    .show_dialog(true)
	    .build();

	  public static void authorizationCodeUri_Sync() {
	    final URI uri = authorizationCodeUriRequest.execute();

	    System.out.println(uri.toString());
	  }

	  public static void authorizationCodeUri_Async() {
	    try {
	      final CompletableFuture<URI> uriFuture = authorizationCodeUriRequest.executeAsync();

	      // Thread free to do other tasks...

	      // Example Only. Never block in production code.
	      final URI uri = uriFuture.join();

	      System.out.println(uri.toString());
	    } catch (CompletionException e) {
	      System.out.println("Error: " + e.getCause().getMessage());
	    } catch (CancellationException e) {
	      System.out.println("Async operation cancelled.");
	    }
	  }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    authorizationCodeUri_Sync();

	}

}
