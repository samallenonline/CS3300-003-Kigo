const express = require('express');
const dotenv = require('dotenv'); // contains SPOTIFY_CLIENT_ID, SPOTIFY_CLIENT_SECRET, REDIRECT_URI
dotenv.config();
const axios = require('axios'); // for HTTP requests
const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');
const { promisify } = require('util');

const app = express();
const readdir = promisify(fs.readdir);

// set port for backend
const PORT = process.env.PORT || 52000;

// define paths
const jarPathNoah = path.join(__dirname, 'libs', 'kigonoah-0.0.1-SNAPSHOT.jar');
const jarPathKelly = path.join(__dirname, 'libs', 'kigokelly-0.0.1-SNAPSHOT.jar');
const lyricsFolder = path.join(__dirname, '../kigonoah/lyrics');
const haikuFolder = path.join(__dirname, '../kigokelly/haikus');

// check if haiku output folder exists
if (!fs.existsSync(haikuFolder)) {
  fs.mkdirSync(haikuFolder);
}

// get frontend URL from .env, default to port 3000 if not available 
const FRONTEND_URL = process.env.REACT_APP_GITHUB_PAGES_URL || 'http://localhost:3000';

// root route
app.get('/', (req, res) => {
  res.send('Welcome to the Kigo Node.js app!');
});

// route to start spotify authorization
app.get('/login', (req, res) => {
  const authorizeURL = `https://accounts.spotify.com/authorize?client_id=${process.env.SPOTIFY_CLIENT_ID}&response_type=code&redirect_uri=${process.env.REDIRECT_URI}&scope=user-top-read`;
  console.log(`[DEBUG] Redirecting user to: ${authorizeURL}`);
  res.redirect(authorizeURL);
});

// callback route after spotify redirects back with the code
app.get('/callback', async (req, res) => {
  const { code } = req.query;

  // if authorization code is missing
  if (!code) {
    console.error('[DEBUG] Missing authorization code');
    return res.redirect(`${FRONTEND_URL}/?success=false`);
  }

  // print authorization code
  console.log(`[DEBUG] Received authorization code: ${code}`);

  try {
    // exchange authorization code for access token
    const response = await axios.post('https://accounts.spotify.com/api/token', null, {
      params: {
        grant_type: 'authorization_code',
        code,
        redirect_uri: process.env.REDIRECT_URI,
        client_id: process.env.SPOTIFY_CLIENT_ID,
        client_secret: process.env.SPOTIFY_CLIENT_SECRET,
      },
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });

	// access token successfully obtained
    const { access_token } = response.data;
    console.log(`[DEBUG] Access token received: ${access_token}`);

    // fetch spotify user's profile to get display name and profile picture
    const profileResponse = await axios.get('https://api.spotify.com/v1/me', {
      headers: {
        Authorization: `Bearer ${access_token}`,
      },
    });

    const displayName = profileResponse.data.display_name;
    const profilePicture =
      profileResponse.data.images && profileResponse.data.images.length > 0
        ? profileResponse.data.images[0].url
        : null;

	// for debugging 
    console.log(`[DEBUG] Spotify account name: ${displayName}`);
    console.log(`[DEBUG] Spotify profile picture URL: ${profilePicture}`);

    // redirect back to react app with success status, display name, and profile picture
    res.redirect(
      `http://localhost:3000/?success=true&displayName=${encodeURIComponent(displayName)}&profilePicture=${encodeURIComponent(
        profilePicture || ''
      )}`
    );
  } catch (error) {
    console.error('[DEBUG] Failed to complete Spotify authentication:', error.message);
    res.redirect(`${FRONTEND_URL}/?success=false`);
  }
});

// route to fetch lyrics using kigonoah-0.0.1-SNAPSHOT.jar (jar file for noah's code)
app.get('/fetch-lyrics', (req, res) => {
  const { songTitle, artistName } = req.query;

  // checks if song title or artist name is missing
  if (!songTitle || !artistName) {
    console.error(`[DEBUG] Missing query parameters: songTitle=${songTitle}, artistName=${artistName}`);
    return res.status(400).send('Missing songTitle or artistName query parameters.');
  }
  
  // for debugging
  console.log(`[DEBUG] Fetching lyrics for Song: ${songTitle}, Artist: ${artistName}`);

  // manage JAR process 
  const jarProcess = spawn('java', ['-jar', jarPathNoah, 'lyrics', songTitle, artistName]);
  let output = '';
  let error = '';

  jarProcess.stdout.on('data', (data) => {
    console.log(`[DEBUG] JAR STDOUT: ${data}`);
    output += data.toString();
  });

  jarProcess.stderr.on('data', (data) => {
    console.error(`[DEBUG] JAR STDERR: ${data}`);
    error += data.toString();
  });

  jarProcess.on('close', (code) => {
    if (code !== 0) {
      console.error(`[DEBUG] JAR exited with code ${code}`);
      res.status(500).send('Failed to fetch lyrics.');
    } else {
      console.log(`[DEBUG] Lyrics fetched: ${output.trim()}`);
      res.send(output.trim());
    }
  });
});

// route to generate haikus using kigokelly-0.0.1-SNAPSHOT.jar (jar file for kelly's code)
app.get('/generate-haiku', async (req, res) => {
  console.log(`[DEBUG] Generating haikus from lyrics folder: ${lyricsFolder}`);

  // checks /lyric folder for presence of .txt files 
  try {
    const files = await readdir(lyricsFolder);
    const lyricFiles = files.filter((file) => file.endsWith('.txt'));

    if (lyricFiles.length === 0) {
      console.log('[DEBUG] No lyric files found in the lyrics folder.');
      return res.status(200).json({ success: false, message: 'No lyric files found to process.' });
    }

    let haikusGenerated = [];

	// manage JAR process
    for (const file of lyricFiles) {
      const inputFilePath = path.join(lyricsFolder, file);

      console.log(`[DEBUG] Processing lyric file: ${file}`);

      const jarProcess = spawn('java', ['-jar', jarPathKelly, inputFilePath], {
        cwd: path.resolve(__dirname, '../kigokelly'),
      });

      let output = '';
      let error = '';

      jarProcess.stdout.on('data', (data) => {
        output += data.toString();
      });

      jarProcess.stderr.on('data', (data) => {
        error += data.toString();
      });

      await new Promise((resolve, reject) => {
        jarProcess.on('close', (code) => {
          if (code !== 0) {
            console.error(`[DEBUG] JAR exited with code ${code} for file: ${file}`);
            console.error(`[DEBUG] JAR STDERR: ${error.trim()}`);
            reject(new Error(`Failed to generate haiku for ${file}`));
          } else {
            console.log(`[DEBUG] Haiku generated for ${file}:\n${output.trim()}`);
            haikusGenerated.push({ file, haiku: output.trim() });
            resolve();
          }
        });
      });
    }

	// for debugging - reports haiku generation success status 
    console.log(`[DEBUG] Haikus generated: ${haikusGenerated.length}`);
    return res.status(200).json({ success: true, message: `Haikus successfully generated for ${haikusGenerated.length} files!` });
  } catch (err) {
    console.error('[DEBUG] Error generating haikus:', err.message);
    return res.status(500).json({ success: false, message: 'Error occurred while generating haikus.' });
  }
});

// route to fetch haikus
app.get('/get-haikus', async (req, res) => {
  try {
    // read all files in the /haikus folder
    const files = await readdir(haikuFolder);
    const haikus = {};

    for (const file of files) {
      const filePath = path.join(haikuFolder, file);
      const content = fs.readFileSync(filePath, 'utf8');

      // extract the first three lines from the file (may change this, each txt file has 3 haikus)
      const lines = content.split('\n').filter(line => line.trim() !== '');
      const haiku = lines.slice(0, 3).join('\n'); // get first 3 lines

      if (haiku) {
        haikus[file] = haiku;
      }
    }

	// for debugging - reports haiku fetch success status
    console.log(`[DEBUG] Fetched haikus: ${Object.keys(haikus).length} files`);
    res.json({ success: true, haikus });
  } catch (error) {
    console.error('[DEBUG] Error reading haikus:', error);
    res.status(500).json({ success: false, message: 'Failed to fetch haikus.' });
  }
});

// report port being used for local testing
app.listen(PORT, () => {
  console.log(`[DEBUG] Server running on http://localhost:${PORT}`);
});

