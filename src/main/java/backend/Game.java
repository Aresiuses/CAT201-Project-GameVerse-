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
}
