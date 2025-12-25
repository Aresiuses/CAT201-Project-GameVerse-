package backend;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;   // List of all games in the cart

    public Cart() {
        items = new ArrayList<>();  // Initialize empty cart
    }

    // Add a game to the cart
    public void addGame(Game game, int quantity) {
        // Check if the game is already in the cart
        for (CartItem item : items) {
            if (item.getGame().getId() == game.getId()) { // assuming Game has getId()
                // If already in cart, increase the quantity
                item.setQuantity(item.getQuantity() + quantity);
                return; // exit because we updated existing item
            }
        }
        // If game not already in cart, add as new CartItem
        items.add(new CartItem(game, quantity));
    }

    // Remove a game completely from the cart
    public void removeGame(Game game) {
        items.removeIf(item -> item.getGame().getId() == game.getId());
    }

    // Get list of all items in the cart
    public List<CartItem> getItems() {
        return items;
    }

    // Calculate total price of the cart
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice(); // add up total price of each item
        }
        return total;
    }
}


