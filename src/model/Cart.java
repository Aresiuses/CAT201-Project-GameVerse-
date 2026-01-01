package src.model;

import src.model.Game;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<Game> items;

    // Constructor
    public Cart() {
        this.items = new ArrayList<>();
    }

    // Add a game to the cart
    public void add(Game game) {
        items.add(game);
    }

    // Remove a game from the cart
    public void remove(Game game) {
        items.remove(game);
    }

    // Get all items in the cart
    public List<Game> getItems() {
        return items;
    }

    // Get total price of all items
    public double getTotalPrice() {
        return items.stream().mapToDouble(Game::getPrice).sum();
    }

    // Clear the cart
    public void clear() {
        items.clear();
    }
}
