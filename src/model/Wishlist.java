package src.model;

import java.util.ArrayList;
import java.util.List;

public class Wishlist {

    private int userId;
    private List<String> gameIds;

    public Wishlist() {
        gameIds = new ArrayList<>();
    }

    public Wishlist(int userId) {
        this.userId = userId;
        this.gameIds = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public List<String> getGameIds() {
        return gameIds;
    }
}
