package backend;

import java.io.*;
import java.util.*;

public class GameStore {

    public static List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();

        try (InputStream is = GameStore.class.getClassLoader().getResourceAsStream("data/games.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4)
                    games.add(new Game(parts[0], parts[1], parts[2], Double.parseDouble(parts[3])));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return games;
    }
}
