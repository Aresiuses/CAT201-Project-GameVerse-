package src.data;

import src.model.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import src.service.GameDataAPIService;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatabaseHandler {
    private static final String FILE_PATH = "gameverse_data.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private List<Game> games;

    public DatabaseHandler() {
        this.games = loadGameFromFile();
        if (this.games.isEmpty()) {
            initializeWithMockData();
            saveGamesToFile();
        }

        // --- API Test Call ---
        System.out.println("\n--- Initiating IGDB API Test ---");

        // 1. Instantiate the API Service
        GameDataAPIService apiService = new GameDataAPIService();

        // 2. Call the API for a known game
        String testGameTitle = "The Witcher 3"; // Choose any popular game
        apiService.fetchGameMetadata(testGameTitle);

        System.out.println("--- IGDB API Test Complete ---\n");
        // -----------------------
    }

    private List<Game> loadGameFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()|| file.length() == 0){
         return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            Type gameListType = new TypeToken<ArrayList<Game>>() {}.getType();
            List<Game> loadedGames = gson.fromJson(reader, gameListType);
            return loadedGames != null ? loadedGames : new ArrayList<>();
        }
        catch (IOException e) {
            System.err.println("Error loading games from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveGamesToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(games, writer);
        } catch (IOException e) {
            System.err.println("Error saving games to file: " + e.getMessage());
        }
    }

    private void initializeWithMockData() {
        // Initial Mock Data - demonstrates different platforms and genres
        Game g1 = new Game("CyberDrive 2077", "PC", "RPG", 59.99, 150);
        g1.setDescription("A massive open-world RPG set in a futuristic dystopia.");
        g1.setImageURL("https://placehold.co/400x250/cc0000/ffffff?text=PC+CyberDrive");
        games.add(g1);

        Game g2 = new Game("Aetheria Odyssey", "PS5", "Adventure", 69.99, 80);
        g2.setDescription("A journey across magical lands with stunning graphics.");
        g2.setImageURL("https://placehold.co/400x250/003366/ffffff?text=PS5+Aetheria");
        games.add(g2);

        Game g3 = new Game("Tactical Warfare X", "XBOX", "Shooter", 49.99, 200);
        g3.setDescription("Fast-paced tactical multiplayer shooter experience.");
        g3.setImageURL("https://placehold.co/400x250/008000/ffffff?text=XBOX+Warfare");
        games.add(g3);
    }

    public List<Game> getAllGames() {
        return new ArrayList<>(games);
    }

    public Optional<Game> getGameById(String id) {
        return games.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst();
    }

    public List<Game> searchGames(String query, String platform) {
        // Core Java Processing: filtering and searching data
        String lowerQuery = query.toLowerCase();

        return games.stream()
                .filter(g ->
                        g.getTitle().toLowerCase().contains(lowerQuery) ||
                                g.getGenre().toLowerCase().contains(lowerQuery) ||
                                g.getDescription().toLowerCase().contains(lowerQuery)
                )
                .filter(g -> platform == null || platform.isEmpty() || g.getPlatform().equalsIgnoreCase(platform))
                .collect(Collectors.toList());
    }

    public void updateGame(Game updatedGame) {
        // Simple update by replacing the old object with the new one
        this.games = this.games.stream()
                .map(g -> g.getId().equals(updatedGame.getId()) ? updatedGame : g)
                .collect(Collectors.toList());
        saveGamesToFile();
    }

    public void addGame(Game newGame) {
        this.games.add(newGame);
        saveGamesToFile();
    }
}
