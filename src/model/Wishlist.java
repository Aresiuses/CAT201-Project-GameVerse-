package src.model;

import java.util.ArrayList;


public class Wishlist {
    private int userId;
    private ArrayList<String> products;

    public Wishlist(int userId) {
        this.userId = userId;
        this.products = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public ArrayList<String> getProducts() {
        if (this.products == null) this.products = new ArrayList<>();
        return products;
    }
}