import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.SavedTrack;
import se.michaelthelin.spotify.requests.data.library.GetUsersSavedTracksRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


public class GetUsersSavedTracks {

	private static final String accessToken = "BQDtv8K6ixlNY-H3rTEZtBkYpHHwOjo1bEO98QhqOBWLJdeJSR2Bp5xLB6OsVPIbS2gSIAjYaLFLnRnWb-Z3NHVZbw41kznwcQ3fAYJw92-_SyAadO4";

	  private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
	    .setAccessToken(accessToken)
	    .build();
	  private static final GetUsersSavedTracksRequest getUsersSavedTracksRequest = spotifyApi.getUsersSavedTracks()
//	          .limit(10)
//	          .offset(0)
//	          .market(CountryCode.SE)
	    .build();

	  public static void getUsersSavedTracks_Sync() {
	    try {
	      final Paging<SavedTrack> savedTrackPaging = getUsersSavedTracksRequest.execute();

	      System.out.println("Total: " + savedTrackPaging.getTotal());
	    } catch (IOException | SpotifyWebApiException | ParseException e) {
	      System.out.println("Error: " + e.getMessage());
	    }
	  }

	  public static void getUsersSavedTracks_Async() {
	    try {
	      final CompletableFuture<Paging<SavedTrack>> pagingFuture = getUsersSavedTracksRequest.executeAsync();

	      // Thread free to do other tasks...

	      // Example Only. Never block in production code.
	      final Paging<SavedTrack> savedTrackPaging = pagingFuture.join();

	      System.out.println("Total: " + savedTrackPaging.getTotal());
	    } catch (CompletionException e) {
	      System.out.println("Error: " + e.getCause().getMessage());
	    } catch (CancellationException e) {
	      System.out.println("Async operation cancelled.");
	    }
	  }
	  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getUsersSavedTracks_Sync();
	}

}
