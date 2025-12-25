package backend;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Game> purchasedGames;

    public Library() {
        purchasedGames = new ArrayList<>();
    }

    public void addGame(Game game) {
        purchasedGames.add(game);
    }

    public List<Game> getPurchasedGames() {
        return purchasedGames;
    }
}
