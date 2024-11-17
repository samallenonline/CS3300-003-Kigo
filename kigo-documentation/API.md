# API Documentation for Kigo

This document describes the API endpoints used in the Kigo app, along with information relating to error handling and libraries. 

---

## Authentication

### Spotify OAuth 2.0 Flow

Kigo uses Spotify’s OAuth 2.0 for authorization to access user data. Users must log in with Spotify to give permission for accessing their listening history and top songs.

**Required Spotify Scopes:**
- `user-read-private`
- `user-read-email`
- `user-top-read`

### Environment Variables

To configure the app, set up the following environment variables in the `.env` file:

```plaintext
SPOTIFY_CLIENT_ID=<your_spotify_client_id>
SPOTIFY_CLIENT_SECRET=<your_spotify_client_secret>
```

---

## Endpoints

### 1. `/login`
- **Description**: Redirects the user to Spotify’s authorization page.
- **Method**: `GET`
- **URL**: `/login`
- **Response**: Redirects to Spotify for login; after authentication, Spotify redirects back to `/callback` with an authorization code.
- **Parameters**: None

### 2. `/callback`
- **Description**: Handles Spotify’s redirection and exchanges the authorization code for an access token.
- **Method**: `GET`
- **URL**: `/callback`
- **Query Parameters**:
  - `code`: The authorization code returned from Spotify.
- **Response**: After successful authentication, redirects the user back to the front end with their display name as a query parameter.
- **Error Handling**: Returns an error message if authentication fails.

---

## Error Handling

The app logs error messages if issues occur during:
- Spotify authentication
- Data retrieval from Spotify (e.g., song history)

For detailed responses and error codes, refer to the error handling setup in `index.js`.

---

## Libraries

- **spotify-web-api-node**: Communicates with Spotify for user authorization and data retrieval.
- **express**: Used as the server framework.
- **dotenv**: Loads environment variables for API keys and configuration.
