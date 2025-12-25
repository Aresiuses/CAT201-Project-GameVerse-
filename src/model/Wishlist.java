package models;

import java.util.ArrayList;

public class Wishlist {
    private String userId;
    private ArrayList<String> products;

    public Wishlist(String userId) {
        this.userId = userId;
        this.products = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<String> getProducts() {
        return products;
    }
}
