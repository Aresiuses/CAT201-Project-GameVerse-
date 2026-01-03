package src.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    public enum Role {
        ADMIN,
        CUSTOMER
    }

    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private Role role;
    private List<String> ownedGameIds;
    private Cart cart;

    public User(int userId, String username, String email,
                String passwordHash, Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.ownedGameIds = new ArrayList<>();
        this.cart = new Cart();
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username;}
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Role getRole() { return role; }
    public List<String> getOwnedGameIds() { return ownedGameIds; }

    public void addOwnedGame(String gameId) {
        if (ownedGameIds == null) ownedGameIds = new ArrayList<>();
        if (!ownedGameIds.contains(gameId)) ownedGameIds.add(gameId);
    }

    public Cart getCart() {
        if (this.cart == null) this.cart = new Cart();
        return cart;
    }
    public void setCart(Cart cart) { this.cart = cart; }
}