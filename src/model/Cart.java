package src.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;   // List of all games in the cart
    private double totalPrice;

    public Cart() {
        this.items = new ArrayList<>();  // Initialize empty cart
        this.totalPrice = 0.0;
    }

    // Add a game to the cart
    public void addGame(Game game, int quantity, String platform) {
        boolean found = false;
        for (CartItem item : items) {
            if (item.getGame().getId().equals(game.getId()) && item.getSelectedPlatform().equals(platform)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            items.add(new CartItem(game, quantity, platform));
        }
        updateTotal();
    }

    // Remove a game completely from the cart
    public void removeGame(String gameId) {
        items.removeIf(item -> item.getGame().getId().equals(gameId));
        updateTotal();
    }

    // Get list of all items in the cart
    public List<CartItem> getItems() {
        return items;
    }

    public void updateTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        this.totalPrice = total;
    }

    public double getTotalPrice() {
        updateTotal();
        return totalPrice;
    }

    public void clear() {
        items.clear();
        totalPrice = 0.0;
    }
}


