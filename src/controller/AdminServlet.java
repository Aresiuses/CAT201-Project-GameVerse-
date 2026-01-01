package src.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import src.data.DatabaseHandler;
import src.model.Game;
import src.model.User;
import src.service.GameDataAPIService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/api/admin")
public class AdminServlet extends HttpServlet {
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final GameDataAPIService apiService = new GameDataAPIService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser == null || loggedUser.getRole() != User.Role.ADMIN) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("{\"status\":\"error\",\"message\":\"Unauthorized access\"}");
            return;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
        String title = json.get("title").getAsString();
        double price = json.get("price").getAsDouble();
        List<String> platforms = new ArrayList<>();
        json.get("platforms").getAsJsonArray().forEach(p -> platforms.add(p.getAsString()));

        Optional<Game> apiGame = apiService.fetchGameMetadata(title);
        if (apiGame.isPresent()) {
            Game g = apiGame.get();
            g.setPlatforms(platforms);
            g.setPrice(price);
            dbHandler.addGame(g);
            response.getWriter().print("{\"status\":\"success\",\"message\":\"Game added successfully!\"}");
        } else {
            response.setStatus(404);
            response.getWriter().print("{\"status\":\"error\",\"message\":\"Metadata not found\"}");
        }
    }
}