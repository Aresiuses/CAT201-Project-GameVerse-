package src.service;

import src.model.Game;
import com.google.gson.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameDataAPIService {
    private static final String TWITCH_CLIENT_ID = "9q99xss4dstxdjow2uzta2ueqicwbq";
    private static final String TWITCH_CLIENT_SECRET = "ze0229j20ipl8snluerip8o04mscmk";
    private static final String TWITCH_TOKEN_URL = "https://id.twitch.tv/oauth2/token";
    private static final String IGDB_API_URL = "https://api.igdb.com/v4/games";

    private static String accessToken = null;

    private synchronized String getAccessToken() {
        if (accessToken != null) return accessToken;
        try {
            URL url = new URL(TWITCH_TOKEN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            String body = String.format("client_id=%s&client_secret=%s&grant_type=client_credentials", TWITCH_CLIENT_ID, TWITCH_CLIENT_SECRET);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
            if (connection.getResponseCode() != 200) return null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                JsonObject json = JsonParser.parseString(br.lines().collect(Collectors.joining())).getAsJsonObject();
                accessToken = json.get("access_token").getAsString();
                return accessToken;
            }
        } catch (Exception e) { return null; }
    }

    public Optional<Game> fetchGameMetadata(String gameTitle) {
        String token = getAccessToken();
        if (token == null) return Optional.empty();

        try {
            URL url = new URL(IGDB_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Client-ID", TWITCH_CLIENT_ID);
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setDoOutput(true);

            // Expanded query to include release date and companies
            String query = String.format(
                    "fields name, summary, cover.url, genres.name, platforms.name, first_release_date, " +
                            "involved_companies.developer, involved_companies.publisher, involved_companies.company.name; " +
                            "search \"%s\"; limit 1;", gameTitle.replace("\"", ""));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(query.getBytes(StandardCharsets.UTF_8));
            }

            if (conn.getResponseCode() != 200) return Optional.empty();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JsonArray results = JsonParser.parseString(br.lines().collect(Collectors.joining())).getAsJsonArray();
                if (results.size() == 0) return Optional.empty();

                JsonObject data = results.get(0).getAsJsonObject();
                Game game = new Game();
                game.setTitle(data.has("name") ? data.get("name").getAsString() : gameTitle);
                game.setDescription(data.has("summary") ? data.get("summary").getAsString() : "No description available.");

                if (data.has("cover")) {
                    game.setImageURL(data.getAsJsonObject("cover").get("url").getAsString().replace("t_thumb", "t_cover_big"));
                }

                if (data.has("genres")) {
                    game.setGenre(data.getAsJsonArray("genres").get(0).getAsJsonObject().get("name").getAsString());
                }

                // Parse Release Date
                if (data.has("first_release_date")) {
                    long timestamp = data.get("first_release_date").getAsLong();
                    String formattedDate = Instant.ofEpochSecond(timestamp)
                            .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
                    game.setReleaseDate(formattedDate);
                }

                // Parse Developer and Publisher
                if (data.has("involved_companies")) {
                    JsonArray companies = data.getAsJsonArray("involved_companies");
                    for (JsonElement el : companies) {
                        JsonObject compObj = el.getAsJsonObject();
                        String name = compObj.getAsJsonObject("company").get("name").getAsString();
                        if (compObj.get("developer").getAsBoolean()) game.setDeveloper(name);
                        if (compObj.get("publisher").getAsBoolean()) game.setPublisher(name);
                    }
                }

                return Optional.of(game);
            }
        } catch (Exception e) { return Optional.empty(); }
    }
}