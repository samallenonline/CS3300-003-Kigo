package com.kigo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SpotifyAuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String CLIENT_ID = "ada4a0be2a2d44aca31afd57c310ecf7";
    private static final String CLIENT_SECRET = "d77cc49482cd489f80ac0efaae6f7e7d";
    private static final String REDIRECT_URI = "http://localhost:3000/callback";
    static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(CLIENT_ID)
            .setClientSecret(CLIENT_SECRET)
            .setRedirectUri(SpotifyHttpManager.makeUri(REDIRECT_URI))
            .build();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code == null) {
            // Step 1: Redirect user to Spotify's authorization page
            String authorizationUrl = "https://accounts.spotify.com/authorize?" +
                    "client_id=" + CLIENT_ID +
                    "&response_type=code" +
                    "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8) +
                    "&scope=" + URLEncoder.encode("user-read-private user-read-email", StandardCharsets.UTF_8);

            System.out.printf("%s",authorizationUrl);
            response.sendRedirect(authorizationUrl);
        } else {
            // Step 2: Handle Spotify's redirect with authorization code
            try {
                AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
                AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

                // Step 3: Store the access and refresh tokens
                spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
                spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

                response.setContentType("text/html");
                response.getWriter().write("<h1>Successfully authenticated with Spotify!</h1>");
                response.getWriter().write("<p>Access Token: " + authorizationCodeCredentials.getAccessToken() + "</p>");
            } catch (SpotifyWebApiException | ParseException e) {
                response.getWriter().write("Error: " + e.getMessage());
            }
        }
    }
}
