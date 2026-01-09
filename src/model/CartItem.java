package src.model;

public class CartItem {
    private Game game;      // The game this cart item represents
    private int quantity;   // How many copies of the game the user wants
    private String selectedPlatform;

    public CartItem(Game game, int quantity, String selectedPlatform) {
        this.game = game;           // Store the game object
        this.quantity = quantity;   // Store the quantity
        this.selectedPlatform = selectedPlatform;
    }

    // Getter and Setters
    public Game getGame() {
        return game;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getTotalPrice() {
        return game.getPrice() * quantity; // price per game multiplied by quantity
    }
    public String getSelectedPlatform() {return selectedPlatform;}
}
