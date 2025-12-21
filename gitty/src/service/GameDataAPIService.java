package src.service;

import src.model.Game;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * GameDataAPIService.java - IGDB Integration
 * Handles authentication via Twitch and fetching game metadata using IGDB's APICalypse query language.
 */
public class GameDataAPIService {

    // --- Configuration: REPLACE WITH YOUR ACTUAL KEYS ---
    // 1. Twitch/IGDB Credentials
    private static final String TWITCH_CLIENT_ID = "YOUR_TWITCH_CLIENT_ID";
    private static final String TWITCH_CLIENT_SECRET = "YOUR_TWITCH_CLIENT_SECRET";

    // 2. API Endpoints
    private static final String TWITCH_TOKEN_URL = "https://id.twitch.tv/oauth2/token";
    private static final String IGDB_API_URL = "https://api.igdb.com/v4/games";

    // Internal state to store the access token
    private static String accessToken = null;

    // JSON Utility
    private final Gson gson = new Gson();

    /**
     * Step 1: Fetches or refreshes the Twitch OAuth Access Token required for IGDB API calls.
     * This is an essential networking step for IGDB integration.
     */
    private synchronized String getAccessToken() {
        if (accessToken != null) {
            return accessToken; // Return existing token if already fetched
        }

        try {
            URL url = new URL(TWITCH_TOKEN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Build the body for the token request
            String requestBody = String.format(
                    "client_id=%s&client_secret=%s&grant_type=client_credentials",
                    TWITCH_CLIENT_ID, TWITCH_CLIENT_SECRET
            );

            // Write the body to the connection
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to fetch Twitch Access Token. Response Code: " + connection.getResponseCode());
                return null;
            }

            // Read and parse the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseText = br.lines().collect(Collectors.joining());
                JsonObject jsonResponse = JsonParser.parseString(responseText).getAsJsonObject();

                accessToken = jsonResponse.get("access_token").getAsString();
                int expiresIn = jsonResponse.get("expires_in").getAsInt();
                System.out.println("Successfully fetched new Twitch Access Token. Expires in " + expiresIn + " seconds.");

                // TODO: Implement token expiry check and refresh logic for production readiness

                return accessToken;
            }
        } catch (Exception e) {
            System.err.println("Error during Twitch token request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Step 2: Fetches detailed game data from the IGDB API based on a title.
     * @param gameTitle The title of the game to search for.
     * @return An Optional containing a partial Game object with API details.
     */
    public Optional<Game> fetchGameMetadata(String gameTitle) {
        String token = getAccessToken();
        if (token == null) {
            System.err.println("Cannot proceed with IGDB query: Access Token is missing.");
            return Optional.empty();
        }

        try {
            URL url = new URL(IGDB_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // --- Set IGDB Headers (Crucial) ---
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Client-ID", TWITCH_CLIENT_ID);
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // --- Build APICalypse Query (POST body) ---
            String igdbQuery = String.format(
                    "fields name, summary, cover.url, genres.name, platforms.name; search \"%s\"; limit 1;",
                    gameTitle.replace("\"", "") // Sanitize input
            );

            // Write the query body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = igdbQuery.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("IGDB Query failed. Response Code: " + connection.getResponseCode());
                // Print the error stream for debugging IGDB errors
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    String error = br.lines().collect(Collectors.joining());
                    System.err.println("IGDB Error Response: " + error);
                }
                return Optional.empty();
            }

            // Read the JSON Response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseText = br.lines().collect(Collectors.joining());

                // --- DEBUGGING LINE ADDED HERE ---
                System.out.println("--- IGDB RAW JSON RESPONSE START ---");
                System.out.println(responseText);
                System.out.println("--- IGDB RAW JSON RESPONSE END ---");
                // ---------------------------------

                // IGDB returns an array of results, so we parse the first element
                JsonElement rootElement = JsonParser.parseString(responseText);
                if (!rootElement.isJsonArray() || rootElement.getAsJsonArray().size() == 0) {
                    System.out.println("IGDB: No results found for " + gameTitle);
                    return Optional.empty();
                }

                JsonObject gameData = rootElement.getAsJsonArray().get(0).getAsJsonObject();

                // --- Extract and Map Data (The 'Processing' part) ---
                Game apiDetails = new Game();
                apiDetails.setTitle(gameData.has("name") ? gameData.get("name").getAsString() : gameTitle);
                apiDetails.setDescription(gameData.has("summary") ? gameData.get("summary").getAsString() : "No summary available from IGDB.");

                // Handle Cover Image URL
                if (gameData.has("cover") && gameData.get("cover").isJsonObject()) {
                    String coverUrl = gameData.getAsJsonObject("cover").get("url").getAsString();
                    // IGDB returns a relative path, convert to absolute (t_cover_big is a thumbnail size)
                    apiDetails.setImageURL(coverUrl.replace("t_thumb", "t_cover_big"));
                }

                // Handle Genre (take the first one)
                if (gameData.has("genres") && gameData.get("genres").isJsonArray() && gameData.getAsJsonArray("genres").size() > 0) {
                    String genreName = gameData.getAsJsonArray("genres").get(0).getAsJsonObject().get("name").getAsString();
                    apiDetails.setGenre(genreName);
                }

                System.out.println("Successfully fetched and parsed data for: " + apiDetails.getTitle());
                return Optional.of(apiDetails);
            }

        } catch (Exception e) {
            System.err.println("General Error during IGDB call: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
