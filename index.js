// ignore warnings - do not affect functionality in a significant way
const express = require('express');
const SpotifyWebApi = require('spotify-web-api-node');
const dotenv = require('dotenv');

dotenv.config(); // load environment variables from .env file

const app = express();
const PORT = process.env.PORT || 3000;

// spotify API setup with credentials from environment variables
const spotifyApi = new SpotifyWebApi({
  clientId: process.env.SPOTIFY_CLIENT_ID,
  clientSecret: process.env.SPOTIFY_CLIENT_SECRET,
  redirectUri: 'https://kigo-app.glitch.me/login' 
});

// route to start Spotify authorization
app.get('/login', (req, res) => {
  console.log("Received request to /login"); // for debugging
  const authorizeURL = spotifyApi.createAuthorizeURL(['user-read-private', 'user-read-email']);
  res.redirect(authorizeURL);
});

// callback route after authorization
app.get('/callback', async (req, res) => {
  console.log("Received request to /callback"); // for debugging
  const code = req.query.code;

  try {
    const data = await spotifyApi.authorizationCodeGrant(code);
    spotifyApi.setAccessToken(data.body['access_token']);
    spotifyApi.setRefreshToken(data.body['refresh_token']);
	
	// get user's spotify profile
	const userData = await spotifyApi.getMe();
	const displayName = userData.body.display_name;

	
	// redirect back to the website with  display name as query parameter
	res.redirect(`https://samallenonline.github.io/CS3300-003-Kigo?displayName=${encodeURIComponent(displayName)}`);
  } catch (error) {
    console.error("Error during Spotify authentication:", error);
    res.send("Authentication failed.");
  }
});

// start the server
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
