package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private List<Integer> ownedGameIds;

    public User(int userId, String username, String email, String passwordHash) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.ownedGameIds = new ArrayList<>();
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public List<Integer> getOwnedGameIds() { return ownedGameIds; }

    public void addGame(int gameId) {
        ownedGameIds.add(gameId);
    }
}
