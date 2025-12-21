package backend;

import java.util.List;

public class JsonHelper {

    public static String gameToJson(Game g) {
        return String.format(
                "{\"id\":\"%s\",\"title\":\"%s\",\"platform\":\"%s\",\"price\":%.2f}",
                g.getId(),
                g.getTitle(),
                g.getPlatform(),
                g.getPrice()
        );
    }

    public static String gameListToJson(List<Game> games) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < games.size(); i++) {
            sb.append(gameToJson(games.get(i)));
            if (i < games.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
