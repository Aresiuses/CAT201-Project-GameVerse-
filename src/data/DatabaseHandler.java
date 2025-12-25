package src.data;

import src.model.Game;
import src.model.User;
import src.model.User.Role;
import src.model.Wishlist;
import src.service.GameDataAPIService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseHandler {

    // ================= GAME DATABASE =================
    private static final String GAME_DB_PATH = "gameverse_data.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private List<Game> games;

    // ================= USER DATABASE =================
    private static final String USER_DB_PATH = "src/data/users.json";
    private static List<User> users = new ArrayList<>();

    // ================= WISHLIST DATABASE =================
    private static final String WISHLIST_DB_PATH = "src/data/wishlists.json";
    private static List<Wishlist> wishlists = new ArrayList<>();

    // ================= CONSTRUCTOR =================
    public DatabaseHandler() {

        // ---- GAME INIT ----
        this.games = loadGamesFromFile();
        if (games.isEmpty()) {
            initializeWithMockData();
            saveGamesToFile();
        }

        // ---- USER INIT ----
        loadUsers();

        // ---- WISHLIST INIT ----
        loadWishlists();

        // ---- IGDB API TEST ----
        System.out.println("\n--- Initiating IGDB API Test ---");
        GameDataAPIService apiService = new GameDataAPIService();
        apiService.fetchGameMetadata("The Witcher 3");
        System.out.println("--- IGDB API Test Complete ---\n");
    }

    // ==================================================
    // ================= GAME METHODS ===================
    // ==================================================

    private List<Game> loadGamesFromFile() {
        File file = new File(GAME_DB_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<ArrayList<Game>>() {}.getType();
            List<Game> data = gson.fromJson(reader, type);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void saveGamesToFile() {
        try (Writer writer = new FileWriter(GAME_DB_PATH)) {
            gson.toJson(games, writer);
        } catch (IOException ignored) {}
    }

    private void initializeWithMockData() {
        games.add(new Game("CyberDrive 2077", "PC", "RPG", 59.99, 150));
        games.add(new Game("Aetheria Odyssey", "PS5", "Adventure", 69.99, 80));
        games.add(new Game("Tactical Warfare X", "XBOX", "Shooter", 49.99, 200));
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
        String q = query.toLowerCase();

        return games.stream()
                .filter(g -> g.getTitle().toLowerCase().contains(q)
                        || g.getGenre().toLowerCase().contains(q))
                .filter(g -> platform == null || platform.isEmpty()
                        || g.getPlatform().equalsIgnoreCase(platform))
                .collect(Collectors.toList());
    }

    // ==================================================
    // ================= USER METHODS ===================
    // ==================================================

    public static void loadUsers() {
        File file = new File(USER_DB_PATH);
        if (!file.exists()) {
            users = new ArrayList<>();
            saveUsers();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            User[] arr = gson.fromJson(reader, User[].class);
            users = arr != null ? new ArrayList<>(Arrays.asList(arr)) : new ArrayList<>();
        } catch (Exception e) {
            users = new ArrayList<>();
        }
    }

    public static User authenticate(String email, String passwordHash) {
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

    // ==================================================
    // ================= WISHLIST METHODS ===============
    // ==================================================

    public static void loadWishlists() {
        File file = new File(WISHLIST_DB_PATH);
        if (!file.exists()) {
            wishlists = new ArrayList<>();
            saveWishlists();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Wishlist[] arr = gson.fromJson(reader, Wishlist[].class);
            wishlists = arr != null ? new ArrayList<>(Arrays.asList(arr)) : new ArrayList<>();
        } catch (Exception e) {
            wishlists = new ArrayList<>();
        }
    }

    public static void saveWishlists() {
        try (Writer writer = new FileWriter(WISHLIST_DB_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(wishlists, writer);
        } catch (IOException ignored) {}
    }

    public boolean addGameToWishlist(int userId, String gameId) {

        boolean gameExists = games.stream()
                .anyMatch(g -> g.getId().equals(gameId));

        if (!gameExists) return false;

        Wishlist wl = wishlists.stream()
                .filter(w -> w.getUserId() == userId)
                .findFirst()
                .orElse(null);

        if (wl == null) {
            wl = new Wishlist(userId);
            wishlists.add(wl);
        }

        if (!wl.getGameIds().contains(gameId)) {
            wl.getGameIds().add(gameId);
            saveWishlists();
        }

        return true;
    }

    public List<Game> getUserWishlist(int userId) {

        Wishlist wl = wishlists.stream()
                .filter(w -> w.getUserId() == userId)
                .findFirst()
                .orElse(null);

        if (wl == null) return new ArrayList<>();

        return games.stream()
                .filter(g -> wl.getGameIds().contains(g.getId()))
                .collect(Collectors.toList());
    }
}
