# Kigo - Lyric-Driven Haiku Generator

Kigo is a web app that generates haikus using lyrics from the user's most-listened songs. This is achieved by utilizing several APIs to pull the user's listening history, identify the user's most-listened songs, and then retrieve lyrics from these songs. Our algorithms count the syllables and parse these lyrics to create personalized, randomly-generated haikus. 

## Getting Started

### Prerequisites
- Node.js is installed on your machine
- A Spotify Developer Account with client ID and client secret
- Access to the [Genius API](https://genius.com/)

### Installation
1. Clone this repository (using bash or GitHub Desktop):

```bash git clone https://github.com/yourusername/CS3300-003-Kigo.git```

2. Install dependencies:

```bash npm install```
	
3. Set up environment variables in a .env file (must be located in the root directory)

```bash
SPOTIFY_CLIENT_ID=your_client_id
SPOTIFY_CLIENT_SECRET=your_client_secret
```

4. Run the React app

```bash npm start```

### Usage 
React should automatically open your browser and redirect you to a local server. Proceed with authenticating your Spotify account by clicking the buttons on the homepage, and enjoy randomly generated haikus based on your Spotify listening history!

### Contributing 
Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

### License
This project is licesed under the MIT license. Please view the LICENSE file for details.

### Credits
- [Spotify API](https://developer.spotify.com/)
- [Genius API](https://docs.genius.com/)
