package src.model;

import java.io.Serializable;
import java.util.UUID;
import java.util.Random;


public class Game implements Serializable {
    private String id;

    private String title;
    private String genre;
    private double price;
    private String platform;

    private double downloadSizeGB;
    private String description;
    private String imageURL;
    private double lowestApiPrice;

    private String developer;
    private String publisher;
    private String releaseDate;

    public Game() {
        this.id = UUID.randomUUID().toString();
        this.price = 0.0;

        Random rand = new Random();
        this.downloadSizeGB = 5.0 + (150.0 - 5.0) * rand.nextDouble();
        this.downloadSizeGB = Math.round(this.downloadSizeGB * 10.0) / 10.0;

        this.developer = "Unavailable";
        this.publisher = "Unavailable";
        this.releaseDate = "TBA";
    }

    public Game(String title, String platform, String genre, double price, double downloadSizeGB ) {
        this();
        this.title = title;
        this.platform = platform;
        this.genre = genre;
        this.price = price;
        this.description = "Good Game";
        this.imageURL = "https://placehold.co/400x250/34495e/ecf0f1?text=" + title.replace(" ", "+");
    }

    public String startDownload() {
        return "Starting Download for " + this.title + " Size: " + this.downloadSizeGB + " GB";
    }

    //getters and setter
    public String getId() {return id; }
    public String getTitle() {return title; }
    public void setTitle(String title) {this.title = title; }
    public String getGenre() {return genre; }
    public void setGenre(String genre) {this.genre = genre; }
    public double getPrice() {return price; }
    public void setPrice(double price) {this.price = price; }
    public String getPlatform() {return platform; }
    public void setPlatform(String platform) {this.platform = platform; }

    public double getDownloadSizeGB() {return downloadSizeGB; }
    public void setDownloadSizeGB(double downloadSizeGB) {this.downloadSizeGB = downloadSizeGB; }
    public String getDescription() {return description; }
    public void setDescription(String description) {this.description = description; }
    public String getImageURL() {return imageURL; }
    public void setImageURL(String imageURL) {this.imageURL = imageURL; }
    public double getLowestApiPrice() {return lowestApiPrice; }
    public void setLowestApiPrice(double lowestApiPrice) {this.lowestApiPrice = lowestApiPrice; }

    public String getDeveloper() { return developer; }
    public void setDeveloper(String developer) { this.developer = developer; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}

