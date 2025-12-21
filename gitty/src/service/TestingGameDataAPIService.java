package src.service;


import src.model.TestingGame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional; // Useful for handling possible null results

/**
 * GameDataAPIService.java
 * Handles communication with external Game Metadata APIs (like RAWG/IGDB).
 * This demonstrates the required complex Java processing, input (from API),
 * and output (parsed data to the Game model).
 */
public class TestingGameDataAPIService {

    // IMPORTANT: Replace with your actual API endpoint and key!
    private static final String API_BASE_URL = "https://api.rawg.io/api/games";
    private static final String API_KEY = "YOUR_API_KEY_HERE"; // Get an actual key

    /**
     * Fetches detailed game data from the external API based on a title.
     * @param gameTitle The title of the game to search for.
     * @return An Optional containing the detailed Game object, or empty if not found/error.
     */
    public Optional<TestingGame> fetchGameDetails(String gameTitle) {
        // 1. Construct the API URL, properly encoding the title
        String searchUrl = API_BASE_URL + "?key=" + API_KEY + "&search=" + gameTitle.replace(" ", "+");

        try {
            // 2. Open the HTTP Connection
            URL url = new URL(searchUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("API Call failed. Response Code: " + responseCode);
                return Optional.empty(); // Handle API errors
            }

            // 3. Read the JSON Response (The "Input" part)
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            // 4. Parse the JSON and map to Game model (The "Processing" part)
            // **TEAM TASK:** Implement JSON parsing here using a library like Gson or simple Java JSON
            // For example: Parse 'content.toString()' and extract title, description, image URL, etc.

            // Mocking the result for the skeleton:
            TestingGame gameFromApi = new TestingGame("AUTO_ID_001", gameTitle, "PC", 59.99, 100);
            /*gameFromApi.setDescription("This is a detailed description fetched from the API.");
            gameFromApi.setGenre("RPG");
            gameFromApi.setImageUrl("https://placehold.co/600x400"); */

            System.out.println("Successfully fetched data for: " + gameFromApi.getTitle());

            // 5. Return the result
            return Optional.of(gameFromApi);

        } catch (Exception e) {
            System.err.println("An error occurred during API call: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // NOTE: A PricingAPIService.java (for CheapShark) would have a similar structure.
}