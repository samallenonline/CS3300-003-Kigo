// imports
const express = require('express');
const dotenv = require('dotenv'); // environment variables
const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');
const { promisify } = require('util');
const cors = require('cors'); // to ensure communication with github pages 
const readdir = promisify(fs.readdir);

dotenv.config();
const app = express();

// check java version 
const javaVersion = spawn('java', ['-version']);

javaVersion.stdout.on('data', (data) => {
  console.log(`Java version: ${data}`);
});

javaVersion.stderr.on('data', (data) => {
  console.log(`Error: ${data}`);
});

javaVersion.on('close', (code) => {
  if (code !== 0) {
    console.log(`Java process exited with code ${code}`);
  }
});

// set port for backend
const PORT = process.env.PORT || 10000;

// allow requests from github pages
const corsOptions = {
  origin: process.env.REACT_APP_GITHUB_PAGES_URL || 'http://localhost:3000',
  methods: ['GET', 'POST'],
  allowedHeaders: ['Content-Type', 'Authorization'],
};

// use CORS middleware
app.use(cors(corsOptions));

// define paths
const jarPathNoah = path.join(__dirname, 'libs', 'kigonoah-0.0.1-SNAPSHOT.jar');
const jarPathKelly = path.join(__dirname, 'libs', 'kigokelly-0.0.1-SNAPSHOT.jar');
const lyricsFolder = path.join(__dirname, 'kigonoah', 'lyrics');
const haikuFolder = path.join(__dirname, 'kigokelly', 'haikus');

// check if output folders exist
if (!fs.existsSync(lyricsFolder)) {
  fs.mkdirSync(lyricsFolder);
}
if (!fs.existsSync(haikuFolder)) {
  fs.mkdirSync(haikuFolder);
}

// get frontend URL from .env, default to port 3000 if not available
const FRONTEND_URL = 'http://localhost:3000';

// root route
app.get('/', (req, res) => {
  res.send('Welcome to the Kigo Node.js app!');
});

// route to start Spotify authorization
app.get('/login', (req, res) => {

  // run JAR file kigonoah-0.0.1-SNAPSHOT.jar (noah's code)
  const jarProcess = spawn('/opt/homebrew/bin/java', ['-jar', jarPathNoah, 'login']);
  let output = ''; // variable to store standard output
  
  // listener for standard output 
  jarProcess.stdout.on('data', (data) => {
    output += data.toString(); // append data to output variable 
  });

  // listener for standard error
  jarProcess.stderr.on('data', (data) => {
    console.error(`[DEBUG] JAR STDERR: ${data}`); // log errors
  });

  // listener for when JAR process closes
  jarProcess.on('close', (code) => {
	// error detected 
    if (code !== 0) {
      console.error(`[DEBUG] JAR exited with code ${code}`);
      res.status(500).send('Failed to generate authorization URI.');
    } else {
	  // no errors
      console.log(`[DEBUG] Authorization URI: ${output.trim()}`);
      res.redirect(output.trim()); // redirect the user to the generated URI
    }
  });
});

// callback route after Spotify redirects back with the code
app.get('/callback', async (req, res) => {
  const { code } = req.query;

  // no authorization code returned
  if (!code) {
    console.error('[DEBUG] Missing authorization code');
    return res.redirect(`${FRONTEND_URL}/?success=false`);
  }

  console.log(`[DEBUG] Received authorization code: ${code}`);

  // run JAR file kigonoah-0.0.1-SNAPSHOT.jar with authorization code
  const jarProcess = spawn('/opt/homebrew/bin/java', ['-jar', jarPathNoah, 'callback', code]);

  let output = ''; // variable to store standard output
  let error = ''; // variable to store errors

  // listener for standard output
  jarProcess.stdout.on('data', (data) => {
    output += data.toString();
  });

  // listener for standard error 
  jarProcess.stderr.on('data', (data) => {
    error += data.toString();
  });

  // listener for when JAR process closes
  jarProcess.on('close', (code) => {
    if (code !== 0) {
      console.error(`[DEBUG] JAR exited with code ${code}`);
      console.error(`[DEBUG] JAR STDERR: ${error.trim()}`);
      return res.redirect(`${FRONTEND_URL}/?success=false`);
    }

    console.log(`[DEBUG] JAR Output: ${output.trim()}`);

    // parse output to extract access token, display name, and profile picture
    const accessToken = output.match(/ACCESS_TOKEN:(.*)/)?.[1]?.trim();
    const displayName = output.match(/DISPLAY_NAME:(.*)/)?.[1]?.trim();
    const profilePicture = output.match(/PROFILE_PICTURE:(.*)/)?.[1]?.trim();

	// if missing access token or displauy name 
    if (!accessToken || !displayName) {
      console.error('[DEBUG] Missing access token or display name in JAR output');
      return res.redirect(`${FRONTEND_URL}/?success=false`);
    }

	// for debugging
    console.log(`[DEBUG] Access Token: ${accessToken}`);
    console.log(`[DEBUG] Display Name: ${displayName}`);
    console.log(`[DEBUG] Profile Picture: ${profilePicture}`);

    // redirect back to front end with information extracted
    res.redirect(
      `${FRONTEND_URL}/?success=true&displayName=${encodeURIComponent(displayName)}&profilePicture=${encodeURIComponent(profilePicture || '')}`
    );
  });
});

// route to fetch lyrics using JAR file kigonoah-0.0.1-SNAPSHOT.jar (noah's code)
app.get('/fetch-lyrics', (req, res) => {
  const { accessToken } = req.query;

  // check if access token was returned 
  if (!accessToken) {
    console.error('[DEBUG] Missing access token');
    return res.status(400).send('Access token is required.');
  }

  // run JAR file kigonoah-0.0.1-SNAPSHOT.jar with access token 
  const jarProcess = spawn('/opt/homebrew/bin/java', ['-jar', jarPathNoah, 'lyrics', accessToken]);

  // listener for standard output
  jarProcess.stdout.on('data', (data) => {
    console.log(`[DEBUG] JAR STDOUT: ${data}`);
  });

  // listener for standard error
  jarProcess.stderr.on('data', (data) => {
    console.error(`[DEBUG] JAR STDERR: ${data}`);
  });

  // listener for when the JAR process closes
  jarProcess.on('close', (code) => {
    if (code !== 0) {
      console.error(`[DEBUG] JAR exited with code ${code}`);
      res.status(500).send('Failed to fetch lyrics.');
    } else {
      res.send('Lyrics fetched and saved successfully.');
    }
  });
});


// route to generate haikus using JAR file kigokelly-0.0.1-SNAPSHOT.jar 
app.get('/generate-haiku', async (req, res) => {
	
  try {
	// for debugging
	console.log(`[DEBUG] Checking contents of lyrics folder: ${lyricsFolder}`);
    const files = await readdir(lyricsFolder);
	console.log(`[DEBUG] Found files: ${files}`);
    const lyricFiles = files.filter((file) => file.endsWith('.txt'));

	// check if files exist in the /lyrics folder 
    if (lyricFiles.length === 0) {
      console.log('[DEBUG] No lyric files found.');
      return res.status(400).send('No lyric files found to process.');
    }

    for (const file of lyricFiles) {
      const filePath = path.join(lyricsFolder, file);

	  // run JAR file kigokelly-0.0.1-SNAPSHOT.jar 
      const jarProcess = spawn('/opt/homebrew/bin/java', ['-jar', jarPathKelly, filePath]);

	  // listener for standard output
      jarProcess.stdout.on('data', (data) => {
        console.log(`[DEBUG] Generating haiku: ${data.toString()}`);
      });

	  // listener for standard error 
      jarProcess.stderr.on('data', (data) => {
        console.error(`[DEBUG] Error in /generate-haiku: ${data.toString()}`);
      });

      await new Promise((resolve, reject) => {
        jarProcess.on('close', (code) => {
          if (code === 0) {
            resolve();
          } else {
            reject(new Error(`Error generating haiku for file: ${file}`));
          }
        });
      });
    }

    res.send({ success: true, message: 'Haikus generated successfully.' });
  } catch (err) {
    console.error(`[DEBUG] Error in /generate-haiku: ${err.message}`);
    res.status(500).send('Error generating haikus.');
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

            // extract first three lines from the file (there are 3 haikus in each file)
            const lines = content.split('\n').filter(line => line.trim() !== '');
            const haiku = lines.slice(0, 3).join('\n'); // get  first 3 lines

            if (haiku) {
                haikus[file] = { file, content: haiku }; // create an object with file and content
            }
        }

        // return the haikus
        console.log(`[DEBUG] Fetched haikus: ${Object.keys(haikus).length} files`);
        res.json({ success: true, haikus });
    } catch (error) {
        console.error('[DEBUG] Error reading haikus:', error);
        res.status(500).json({ success: false, message: 'Failed to fetch haikus.' });
    }
});


// start the server
app.listen(PORT, '0.0.0.0', () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
