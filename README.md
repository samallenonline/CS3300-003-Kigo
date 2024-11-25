# Kigo - Lyric-Driven Haiku Generator

Kigo is a web app that generates haikus using lyrics from the user's most-listened songs. This is achieved by utilizing several APIs to pull the user's listening history, identify the user's most-listened songs, and then retrieve lyrics from these songs. Our algorithms count the syllables and parse these lyrics to create personalized, randomly-generated haikus. 

## Getting Started

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

4. **Other requirements**

- A Spotify Developer Account with a client ID and client secret

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

### Troubleshooting 

- **Node.js or backend issues:**

Ensure all dependencies are installed (npm install) and that the Node.js server is running (node index.js).

- **Spotify authentication issues:**
  
Ensure the Spotify Developer app's Redirect URI is set to:
http://localhost:3000/callback.

- **JAR file not found:**
  
Ensure the compiled JAR files are located in the /libs folder of the kigo project.

### Contributing 
Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

### License
This project is licesed under the MIT license. Please view the LICENSE file for details.

### Credits
- [Spotify API](https://developer.spotify.com/)
