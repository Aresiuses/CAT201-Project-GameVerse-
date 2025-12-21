package src.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import src.model.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameDataAPIServiceRAWG {

    //Tukar dengan API endpoint ngan key yang betul
    private static final String API_BASE_URL = "https://api.igdb.com/v4/games"; //API endpoint
    private static final String API_KEY = "API KEY"; //API Key

    public Optional<Game> fetchGameMetadata(String gameTitle) {

        String searchURL = API_BASE_URL + API_KEY + "/" + gameTitle;

        try {
            URL url = new URL(searchURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("API Call failed. Response Code: " + connection.getResponseCode());
                return Optional.empty();
            }

            // Read the JSON Response (Input Processing)
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content = in.lines().collect(Collectors.joining());
            in.close();

            // **TEAM TASK:** Implement robust JSON parsing here.

            // MOCK IMPLEMENTATION: Simulate parsing and updating a new Game object
            JsonObject jsonResponse = JsonParser.parseString(content).getAsJsonObject();

            // Assuming we successfully parsed the required data:
            Game apiDetails = new Game();
            apiDetails.setDescription("API-fetched: " + jsonResponse.toString().substring(0, 50) + "...");
            apiDetails.setImageURL("https://placehold.co/600x400/1abc9c/ffffff?text=API+IMAGE");
            apiDetails.setGenre("API-Fetched Genre");

            return Optional.of(apiDetails);

        } catch (IOException e) {
            System.err.println("Networking Error during API call: " + e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("JSON Parsing Error: " + e.getMessage());
            return Optional.empty();
        }
    }

}
