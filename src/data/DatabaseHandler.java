package src.data;

import src.model.Game;
import src.model.User;
import src.model.User.Role;
import src.service.GameDataAPIService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
public class DatabaseHandler {

    // ===== GAME DATABASE =====
    private static final String FILE_PATH = "gameverse_data.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private List<Game> games;

    // ===== USER DATABASE (JSON) =====
    private static final String USER_DB_PATH = "users.json";
    private static List<User> users = new ArrayList<>();

    // ---- USER INIT ----
    static {
        loadUsers();
    }

    public DatabaseHandler() {
        // ---- GAME INIT ----
        this.games = loadGamesFromFile();
        if (this.games.isEmpty()) {
            initializeWithMockData();
            saveGamesToFile();
        }


        // ---- IGDB API TEST ----
        System.out.println("\n--- Initiating IGDB API Test ---");
        GameDataAPIService apiService = new GameDataAPIService();
        apiService.fetchGameMetadata("The Witcher 3");
        System.out.println("--- IGDB API Test Complete ---\n");
    }

    // ================= GAME METHODS =================

    private List<Game> loadGamesFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Game>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) { return new ArrayList<>(); }
    }

    public void saveGamesToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(games, writer);
        } catch (IOException ignored) {}
    }

    private void initializeWithMockData() {
        Game g1 = new Game("CyberDrive 2077", Arrays.asList("PC", "PS5", "XBOX"), "RPG", 59.99, 150);
        games.add(g1);

        Game g2 = new Game("Aetheria Odyssey", Arrays.asList("PS5"), "Adventure", 69.99, 80);
        games.add(g2);

        Game g3 = new Game("Tactical Warfare X", Arrays.asList("XBOX"), "Shooter", 49.99, 200);
        games.add(g3);
    }

    public List<Game> getAllGames() {
        return new ArrayList<>(games);
    }

    public Optional<Game> getGameById(String id) {
        return games.stream().filter(g -> g.getId().equals(id)).findFirst();
    }

    public List<Game> searchGames(String query, String platform) {
        String q = query.toLowerCase();
        return games.stream()
                .filter(g -> g.getTitle().toLowerCase().contains(q))
                .filter(g -> platform == null || platform.isEmpty() || g.getPlatforms().contains(platform))
                .collect(Collectors.toList());
    }

    public void addGame(Game game) {
        games.add(game);
        saveGamesToFile();
    }


    // ================= USER METHODS =================

    public static void loadUsers() {
        File file = new File(USER_DB_PATH);
        if (!file.exists()) {
            System.out.println("User file not found at: " + file.getAbsolutePath());
            users = new ArrayList<>();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            User[] arr = gson.fromJson(reader, User[].class);
            users = arr != null ? new ArrayList<>(Arrays.asList(arr)) : new ArrayList<>();
            System.out.println("Loaded " + users.size() + " users.");
        } catch (Exception e) {
            users = new ArrayList<>();
        }
    }

    public static User authenticate(String email, String passwordHash) {
        if (users == null || users.isEmpty()) loadUsers();
        System.out.println("Login attempt: " + email + " with hash " + passwordHash);
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email)
                        && u.getPasswordHash().equals(passwordHash))
                .findFirst()
                .orElse(null);
    }

    public static User registerCustomer(String username, String email, String passwordHash) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) return null;
        }

        int newId = users.size() + 1;
        User user = new User(newId, username, email, passwordHash, Role.CUSTOMER);
        users.add(user);
        saveUsers();
        return user;
    }

    public static void saveUsers() {
        try (Writer writer = new FileWriter(USER_DB_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(users, writer);
        } catch (IOException ignored) {}
    }
}