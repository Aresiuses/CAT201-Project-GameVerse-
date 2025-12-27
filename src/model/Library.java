package src.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Library {

    private List<Game> games;

    // Constructor
    public Library() {
        this.games = new ArrayList<>();
    }

    // Add a game to the library
    public void addGame(Game game) {
        games.add(game);
    }

    // Remove a game from the library
    public void removeGame(Game game) {
        games.remove(game);
    }

    // Get all games
    public List<Game> getAllGames() {
        return games;
    }

    // Find a game by ID
    public Optional<Game> getGameById(String id) {
        return games.stream().filter(game -> game.getId().equals(id)).findFirst();
    }

    // Clear all games
    public void clearLibrary() {
        games.clear();
    }
}
