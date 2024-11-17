import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


public class AuthorizationCode {

	 private static final String clientId = "8b002ca122c7441d8f1f52b9899a8433";
	  private static final String clientSecret = "4394a8e959df4767aea1b463443cd5cc";
	  private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost/callback");
	  private static final String code = "AQCJw79q3papDZudtJ-Rie2Lnc9p_Jlfmopqz-IHvV6wjJjd9FqxhjhxikQW-A9sUkP4lJQIyXKCTiXjp_ThJ1ewCZUnftbomWUAR1F7cueEr-3zO1GFOCjQ7w2z3YQPPeUNj0fiwQJunpFFbaX8MAG_IGTk8Kuk2t_YS3c";

	  private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
	    .setClientId(clientId)
	    .setClientSecret(clientSecret)
	    .setRedirectUri(redirectUri)
	    .build();
	  private static final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
	    .build();

	  public static void authorizationCode_Sync() {
	    try {
	      final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

	      // Set access and refresh token for further "spotifyApi" object usage
	      spotifyApi.setAccessToken("BQBI9IhpPYw2MIjZOGl99Zh57sxUnnfoO8lAWgctfT1IqepS1cFt8iMKOOuvWldA60YIpJAUYBwDMcwXwEHNf0BJpzWHC0ra07vGgg0ouGqTv-TFetU");
	      spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

	      System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
	    } catch (IOException | SpotifyWebApiException | ParseException e) {
	      System.out.println("Error: " + e.getMessage());
	    }
	  }

	  public static void authorizationCode_Async() {
	    try {
	      final CompletableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = authorizationCodeRequest.executeAsync();

	      // Thread free to do other tasks...

	      // Example Only. Never block in production code.
	      final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeCredentialsFuture.join();

	      // Set access and refresh token for further "spotifyApi" object usage
	      spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
	      spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

	      System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
	    } catch (CompletionException e) {
	      System.out.println("Error: " + e.getCause().getMessage());
	    } catch (CancellationException e) {
	      System.out.println("Async operation cancelled.");
	    }
	  }
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		authorizationCode_Sync();
	}

}
