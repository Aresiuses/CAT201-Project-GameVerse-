package src.model;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PurchaseRecord {
    private String orderId;
    private int userId;
    private List<String> gameTitles;
    private double totalPrice;
    private String purchaseDate;

    public PurchaseRecord(int userId, List<String> gameTitles, double totalPrice) {
        this.orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.userId = userId;
        this.gameTitles = gameTitles;
        this.totalPrice = totalPrice;
        this.purchaseDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getOrderId() {return orderId;}
    public int getUserId() {return userId;}
    public List<String> getGameTitles() {return gameTitles;}
    public double getTotalPrice() {return totalPrice;}
    public String getPurchaseDate() {return purchaseDate;}
}
