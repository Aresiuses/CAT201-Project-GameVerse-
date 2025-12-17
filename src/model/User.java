package model;

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
    private List<Integer> ownedGameIds;

    public User(int userId, String username, String email,
                String passwordHash, Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.ownedGameIds = new ArrayList<>();
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public List<Integer> getOwnedGameIds() { return ownedGameIds; }
}
