package com.kigo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LyricsOvhFetcher {
    private static final String API_URL = "https://api.lyrics.ovh/v1/";


    public static String getLyrics(String songTitle, String artistName) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = API_URL + artistName + "/" + songTitle;
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String jsonResponse = response.body().string();

        JsonObject json = JsonParser.parseString(jsonResponse).getAsJsonObject();
        if (json.has("lyrics")) {
            return json.get("lyrics").getAsString();
        } else {
            return "Lyrics not found.";
        }
    }
}

