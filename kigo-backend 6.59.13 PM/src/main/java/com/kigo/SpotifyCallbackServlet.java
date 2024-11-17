package com.kigo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class SpotifyCallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null) {
            // Build the authorization code request
            AuthorizationCodeRequest authorizationCodeRequest = SpotifyAuthServlet.spotifyApi.authorizationCode(code).build();

            try {
                // Execute the request to obtain the authorization code credentials
                AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

                // Store the access and refresh tokens in Spotify API instance
                SpotifyAuthServlet.spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
                SpotifyAuthServlet.spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

                // Redirect to a success page (adjust URL to your app's needs)
                response.sendRedirect("/success");
            } catch (IOException | SpotifyWebApiException | ParseException e) {
                // Handle any exceptions that may occur
                response.getWriter().write("Error during Spotify authentication: " + e.getMessage());
            }
        } else {
            // Handle the case where no authorization code is provided
            response.getWriter().write("Authorization code not provided");
        }
    }
}
