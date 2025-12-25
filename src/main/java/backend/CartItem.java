package backend;

public class CartItem {
    private Game game;      // The game this cart item represents
    private int quantity;   // How many copies of the game the user wants

    public CartItem(Game game, int quantity) {
        this.game = game;           // Store the game object
        this.quantity = quantity;   // Store the quantity
    }

    // Getter for the game object
    public Game getGame() {
        return game;
    }

    // Getter for the quantity
    public int getQuantity() {
        return quantity;
    }

    // Setter for the quantity (can update quantity if needed)
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Calculate the total price for this item
    public double getTotalPrice() {
        return game.getPrice() * quantity; // price per game multiplied by quantity
    }
}
