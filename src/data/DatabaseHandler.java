package src.data;

import src.model.Game;
import src.model.User;
import src.model.CartItem;
import src.model.User.Role;
import src.model.Wishlist;
import src.model.PurchaseRecord;
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
    private static List<Game> games = new ArrayList<>();

    // ===== USER DATABASE (JSON) =====
    private static final String USER_DB_PATH = "users.json";
    private static List<User> users = new ArrayList<>();

    // ===== WISHLIST DATABASE =====
    private static final String WISHLIST_DB_PATH = "wishlists.json";
    private static List<Wishlist> wishlists = new ArrayList<>();

    // ===== PURCHASE HISTORY DATABASE =====
    private static final String HISTORY_DB_PATH = "purchase_history.json";
    private static List<PurchaseRecord> history = new ArrayList<>();

    // ---- USER INIT ----
    static {
        loadUsers();
        loadGamesStatic();
        loadWishlists();
        loadHistory();
    }

    public DatabaseHandler() {
        // ---- GAME INIT ----
        if (games.isEmpty()) {
            initializeWithMockData();
            saveGamesToFile();
        }
    }


    // ================= GAME METHODS =================

    private static void loadGamesStatic() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) return;
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Game>>() {
            }.getType();
            List<Game> loaded = new Gson().fromJson(reader, listType);
            if (loaded != null) games = loaded;
        } catch (IOException e) {
            System.err.println("Error loading games: " + e.getMessage());
        }
    }

    public void saveGamesToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(games, writer);
        } catch (IOException ignored) {
        }
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

    // for library system
    public List<Game> getGamesByIds(List<String> ids) {
        return games.stream()
                .filter(g -> ids.contains(g.getId()))
                .collect(Collectors.toList());
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

    public static boolean finalizePurchase(int userId) {
        Optional<User> userOpt = users.stream().filter(u -> u.getUserId() == userId).findFirst();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> boughtTitles = new ArrayList<>();
            double total = user.getCart().getTotalPrice();

            for (CartItem item : user.getCart().getItems()) {
                user.addToLibrary(item.getGame().getId(), item.getSelectedPlatform());
                boughtTitles.add(item.getGame().getTitle() + " (" + item.getSelectedPlatform() + ")");
            }

            // Create history entry
            if (!boughtTitles.isEmpty()) {
                history.add(new PurchaseRecord(userId, boughtTitles, total));
                saveHistory();
            }

            user.getCart().clear();
            saveUsers();
            return true;
        }
        return false;
    }

    public static void saveUsers() {
        try (Writer writer = new FileWriter(USER_DB_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(users, writer);
        } catch (IOException ignored) {
        }
    }

    // ================= WISHLIST METHODS =================
    public static void loadWishlists() {
        File file = new File(WISHLIST_DB_PATH);
        if (!file.exists()) {
            wishlists = new ArrayList<>();
            return;
        }
        try (Reader reader = new FileReader(file)) {
            Wishlist[] arr = new Gson().fromJson(reader, Wishlist[].class);
            wishlists = arr != null ? new ArrayList<>(Arrays.asList(arr)) : new ArrayList<>();
        } catch (Exception e) {
            wishlists = new ArrayList<>();
        }
    }

    public static void saveWishlists() {
        try (Writer writer = new FileWriter(WISHLIST_DB_PATH)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(wishlists, writer);
        } catch (IOException ignored) {}
    }

    public boolean toggleWishlistItem(int userId, String gameId) {
        boolean gameExists = games.stream().anyMatch(g -> g.getId().equals(gameId));
        if (!gameExists) return false;

        Wishlist wl = wishlists.stream()
                .filter(w -> w.getUserId() == userId)
                .findFirst().orElse(null);

        if (wl == null) {
            wl = new Wishlist(userId);
            wishlists.add(wl);
        }

        boolean isAdded;
        if (wl.getProducts().contains(gameId)) {
            wl.getProducts().remove(gameId); // REMOVE if exists
            isAdded = false;
        } else {
            wl.getProducts().add(gameId); // ADD if new
            isAdded = true;
        }

        saveWishlists();
        return isAdded; // Returns true if added, false if removed
    }

    public List<Game> getUserWishlist(int userId) {
        Wishlist wl = wishlists.stream()
                .filter(w -> w.getUserId() == userId)
                .findFirst().orElse(null);

        if (wl == null) return new ArrayList<>();

        return games.stream()
                .filter(g -> wl.getProducts().contains(g.getId()))
                .collect(Collectors.toList());
    }

    // ================= PURCHASE HISTORY METHODS =================
    private static void loadHistory() {
        File file = new File(HISTORY_DB_PATH);
        if (!file.exists() || file.length() == 0) return;
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<ArrayList<PurchaseRecord>>() {}.getType();
            List<PurchaseRecord> data = new Gson().fromJson(reader, type);
            if (data != null) history = data;
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void saveHistory() {
        try (Writer writer = new FileWriter(HISTORY_DB_PATH)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(history, writer);
        } catch (IOException ignored) {}
    }

    public static List<PurchaseRecord> getHistoryForUser(int userId) {
        return history.stream()
                .filter(r -> r.getUserId() == userId)
                .collect(Collectors.toList());
    }


}