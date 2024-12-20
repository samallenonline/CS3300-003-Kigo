# Kigo Developer Documentation

## Overview

Kigo is a web application that generates personalized haikus using lyrics from a user's most-listened songs. It integrates React for the front end, Node.js for the backend, and Java libraries compiled into JAR files for processing logic. This documentation provides an overview of the app's architecture, key components, and guidance for future developers to maintain or extend the application.

## Architecture Overview

Kigo follows a modular architecture with the following components:

1. **Frontend (React)**

Framework: React.js

Purpose: Provides a visually appealing and interactive user interface for the app, allowing users to navigate the app, authenticate with Spotify, and view personalized haikus.

Key Features:
- GUI that ensures a user-friendly experience for navigating the app's functionality.  
- Spotify authentication and haiku generation flow.
- Dynamic rendering of user-specific data.
- Account preferences stored in local data.

2. **Backend (Node.js)**

Framework: Express.js

Purpose: Acts as a middle man by managing communication between the frontend and backend and ensuring seamless functionality across all components. 

Key Features:
- Serves the React app.
- Manages routes such as /login, /callback, and /generate-haiku.
- Executes Java JAR files to complete various tasks such as retrieving Spotify data, fetching song lyrics, processing these lyrics, and generating personalized haikus. 

3. **Java Libraries (JAR Files)**

Purpose: Provides the core logic for the app, implemented through modular and resuable Java components. 

Components:
- Spotify API integration for retrieving top tracks.
- Lyrics fetching and processing.
- Haiku generation algorithms using syllable counting logic.

## Key Components

1. **Frontend**

Location: kigo/src

Key Files:
- App.js: Manages routing and overall layout.
- Index.js: Initializes the React app.
- Account.js: Displays user preferences and account information.
- App.css: Styling for components defined in App.js.

How It Works: 
Upon visiting the app, the user is presented with a home screen prompting Spotify authentication. Once authenticated, the frontend communicates with the backend for data retrieval and haiku generation. Generated haikus are displayed dynamically.

2. **Backend**

Location: kigo/index.js

Key File:
- index.js: Defines backend routes and communicates with the Java libraries.

Routes:
- /: Root route, displays a welcome message. Offers no functionality and is used for testing purposes only.  
- /login: Redirects the user to Spotify’s authentication page.
- /callback: Handles the redirect after Spotify authentication and processes the access token.
- /fetch-lyrics: Retrieves lyrics from the user's top tracks via the kigonoah JAR file.
- /generate-haiku: Invokes the haiku generation logic via the kigokelly JAR file.
- /get-haiku: Retrieves all haikus contained in the /haiku folder and prepares them to be displayed on the user interface. 
  
How It Works:
The backend uses the Spotify API to retrieve user data and integrates the lyrics.ovh API for lyrics retrieval. Java JAR files are executed using Node.js child_process to handle various processing and logic. 

3. **Java Libraries**

Location: kigokelly and kigonoah

Key Files:

Spotify Authorization and Data Retrieval (kigonoah):
- AuthorizationCode.java: Exchanges the authorization code for an access token.
- AuthorizationCodeUri.java: Generates the Spotify authorization URI.
- CompileLyrics.java: Coordinates the process of fetching relevant user data, retrieving lyrics for the user's top tracks, and storing them as text files in the /lyrics folder. 
- LyricsOvhFetcher.java: Fetches song lyrics using the lyrics.ovh API.
- UserTopTracks.java: Fetches the user's top tracks using the Spotify API.

Lyric Processing and Haiku Generation (kigokelly):
- SyllableCounter1.java: Counts the number of syllables in a given word.
- ProcessLyricLines.java: Cleans and processes raw song lyrics.
- HaikuFinder.java: Finds and generates haikus from processed song lyrics. 
- RemoveDuplicateLines.java: Further processes lyrics by removing duplicate lines.

How It Works:
The Java libraries handle Spotify authentication, data processing, and haiku generation.
These libraries are compiled into JAR files, which the backend executes during runtime.

## Development Workflow

### Prerequisites

Before you begin, ensure you have the following installed on your system:

1. **Node.js**

Node.js is required to run the backend and manage dependencies.  
Install it from the [official Node.js website](https://nodejs.org/).  
After installation, verify it by running:
   
```
node -v
npm -v
```

2. **Maven**

Install Maven from the Apache Maven website.
After installation, verify it by running:

```
mvn -version
```

3. **Java Development Kit (JDK)**

A JDK is required to build and run the Java library.
Install JDK from the Oracle JDK website or use an open-source alternative like OpenJDK.
After installation, verify it by running:

```
java -version
```

**Other requirements**

- A Spotify Developer Account with a client ID and client secret

### Note to TA/grader
- Our Spotify Web app is currently in the development stage, meaning that only users that are added to the authorized users list can access the app. For an easier local setup, the Kigo Team can add you to this list so that you do not have to create a Spotify Developer Account and are able to utilize the .env file provided. If you have not been added to this list, please contact pulsar3k@gmail.com.
- Once you have been added to the list, please ignore all instructions relating to setting up a Spotify Developer Account and .env file.

### Installation
1. Clone only the "kigo-source" branch of this repository (using bash or GitHub Desktop):

```git clone -b kigo-source https://github.com/samallenonline/CS3300-003-Kigo.git```

2. **Import Projects**  
Open your IDE (e.g., Eclipse) and import the following projects:

- **`kigokelly` and `kigonoah`**: Import these as Maven projects.
  - In Eclipse, go to `File > Import > Maven > Existing Maven Projects`.
  - Browse to the folder where `kigokelly` and `kigonoah` are located, and import them.

- **`kigo`**: Import this as a normal project.
  - In Eclipse, go to `File > Import > General > Existing Projects into Workspace`.
  - Select `Import existing files into workspace`, browse to the `kigo` folder, and finish the import.

3. **Install Dependencies for the Node.js App**  
Open a terminal and navigate to the `kigo` directory:

```npm install```
	
3. Set up environment variables in a .env file (must be located in the root directory)

```bash
SPOTIFY_CLIENT_ID=your_client_id
SPOTIFY_CLIENT_SECRET=your_client_secret
REDIRECT_URI=http://localhost:52000/callback
REACT_APP_GITHUB_PAGES_URL=http://localhost:3000
REACT_APP_BACKEND_URL=https://localhost:52000
```

### Project Folder Structure
- `kigo`: Main Node.js backend project.
- `kigokelly`: Java Maven project containing functionality relating to Spotify authorization, retrieving the user's top tracks, and retrieving lyrics for these top tracks.
- `kigonoah`: Java Maven project containing core functionality relating to lyric processing and haiku generation.

### Running the App Locally

1. **Start the React App (Frontend)**
Navigate to the `kigo/docs` directory:

```
cd ../kigo/docs npm start
```
This will start the React app and open it in your browser.

2. **Start the Node.js Backend**  
Open a new terminal window and navigate to the `kigo` directory:

```
cd ../kigo node index.js
```

This will start the Node.js backend at `http://localhost:3000`.

After these steps, you will be able to run the kigo-app locally on your machine. Please reference the user manual if you need more information on navigating the GUI or using certain functionality. 

## Development Tips
**Frontend:**
Use the React Developer Tools browser extension for debugging.
Make changes to components in src and use npm start to view updates in real time.

**Backend:**
Test API routes individually using your terminal.
Debug issues by adding logs in index.js.

**Java Libraries:**
Use an IDE such as Eclipse for better project management.
Write and run unit tests for new classes before integrating them into the JAR files.

## Guidance for Future Developers

**Extending Frontend Functionality:**
Add new pages or features by creating components in src.
Follow the routing structure defined in App.js for navigation.

**Adding New APIs:**
Define new backend routes in index.js and use the child_process module to integrate with external APIs or JAR files.
Ensure that error handling is implemented for API calls.

**Improving Lyric Processing:**
Enhance the SyllableCounter algorithm in kigokelly for better accuracy.
Add tests for edge cases in testingSyllableCounter.java.

**Implementing Error Handling:**
Add detailed error messages for scenarios where APIs fail to return data.
Ensure that the frontend displays meaningful feedback to users during errors.

## Troubleshooting 

- **Node.js or backend issues:**

Ensure all dependencies are installed (npm install) and that the Node.js server is running (node index.js).

- **Spotify authentication issues:**
  
Ensure the Spotify Developer app's Redirect URI is set to:
http://localhost:3000/callback.

- **JAR file not found:**
  
Ensure the compiled JAR files are located in the /libs folder of the kigo project.

## Contributing 
Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License
This project is licensed under the MIT license. Please view the LICENSE file for details.

## Credits
- [Spotify API](https://developer.spotify.com/)
- [lyrics.ovh API](https://lyricsovh.docs.apiary.io/#)
