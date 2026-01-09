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
    private List<String> library;
    private Cart cart;

    public User(int userId, String username, String email,
                String passwordHash, Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.library = new ArrayList<>();
        this.cart = new Cart();
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username;}
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Role getRole() { return role; }
    public List<String> getLibrary() {
        if (library == null) library = new ArrayList<>();
        return library;
    }

    public void addToLibrary(String gameId, String platform) {
        if (library == null) library = new ArrayList<>();
        String entry = gameId + ":" + platform;
        if (!library.contains(entry)) {
            library.add(entry);
        }
    }

    public Cart getCart() {
        if (this.cart == null) this.cart = new Cart();
        return cart;
    }
}