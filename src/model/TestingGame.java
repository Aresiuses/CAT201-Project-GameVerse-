package src.model;

/**
 * Game.java
 * The core data model for a video game in the GameVerse store.
 * This class demonstrates OOP principles (Encapsulation, Data Storage).
 */
public class TestingGame {
    // --- Internal Store Data ---
    private String gameId;          // Unique identifier for the store
    private double localPrice;      // Price the store charges (can be different from API)
    private int stockCount;         // How many digital licenses are in stock
    private String platform;        // e.g., "PC", "PlayStation", "Xbox"

    // --- API Data (Fetched from RAWG/IGDB) ---
    private String title;
    private String genre;
    private String description;
    private String imageUrl;
    private double lowestApiPrice;  // Price fetched from the Pricing API (for comparison)

    // Constructor (For simplicity, use a constructor that accepts key internal data)
    public TestingGame(String gameId, String title, String platform, double localPrice, int stockCount) {
        this.gameId = gameId;
        this.title = title;
        this.platform = platform;
        this.localPrice = localPrice;
        this.stockCount = stockCount;
    }

    // --- Getters and Setters (Demonstrates Encapsulation) ---
    // You MUST include getters and setters for all fields.

    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public double getLocalPrice() { return localPrice; }
    public void setLocalPrice(double localPrice) { this.localPrice = localPrice; }

    public int getStockCount() { return stockCount; }
    public void setStockCount(int stockCount) { this.stockCount = stockCount; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }


    // ... (rest of the getters and setters to be completed by the team) ...

    /**
     * Business Logic Method: Checks if the game is available for purchase.
     * @return true if stock is > 0.
     */
    public boolean isAvailable() {
        return stockCount > 0;
    }

    /**
     * Business Logic Method: Reduces stock after a purchase.
     */
    public void reduceStock() {
        if (stockCount > 0) {
            stockCount--;
        }
    }

    // NOTE: This class will be extended by other models like User, Order, etc.
}