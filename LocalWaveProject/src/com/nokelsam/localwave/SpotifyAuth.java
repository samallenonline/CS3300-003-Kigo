package com.nokelsam.localwave;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SpotifyAuth {
    public static void main(String[] args) {
        try {
            // Your client ID and client secret
            String clientId = "afcc79c9b9ac4a1ea7c49cac21a4256d";
            String clientSecret = "4f35e12388d24c3dad35c42c60aae1df";

            // Encode the client ID and client secret
            String clientCredentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());

            // Construct the URI and then convert it to a URL
            URI uri = new URI("https", "accounts.spotify.com", "/api/token", null);
            URL url = uri.toURL();

            // Set up the connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            // Send the POST request
            String urlParameters = "grant_type=client_credentials";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlParameters.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Print the access token if the request was successful
            if (responseCode == 200) {
                System.out.println("Response: " + response.toString());
            } else {
                System.out.println("Error: " + responseCode);
                System.out.println("Response: " + response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // NEED to have this function return the access token.
    }
}

