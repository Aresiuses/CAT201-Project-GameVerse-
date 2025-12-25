package backend;

public class Game {
    private String id;
    private String title;
    private String platform;
    private double price;

    public Game(String id, String title, String platform, double price) {
        this.id = id;
        this.title = title;
        this.platform = platform;
        this.price = price;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getPlatform() { return platform; }
    public double getPrice() { return price; }

    // ===== ADD THIS =====
    // This allows CartServlet to call getName()
    public String getName() {
        return title;  // Just returns the title of the game
    }

    // Optional: you can also add setName() if needed later
    public void setName(String title) {
        this.title = title;
    }
}

