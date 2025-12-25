package src.controller;

import src.data.DatabaseHandler;
import src.model.Game;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

/**
 * GameServlet.java - Controller Layer
 * Handles all requests related to game viewing, searching, and filtering.
 * Mapped to /api/games.
 */
@WebServlet("/api/games")
public class GameServlet extends HttpServlet {

    // Instantiate Handler once for the application lifetime
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Handles HTTP GET requests to retrieve a list of games (List/Search/Filter).
     * This is the public storefront view.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        String gameId = request.getParameter("id");

        // NEW: If an ID is provided, fetch specific game details
        if (gameId != null && !gameId.isEmpty()) {
            Optional<Game> game = dbHandler.getGameById(gameId);
            if (game.isPresent()) {
                response.getWriter().print(gson.toJson(game.get()));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Game not found");
            }
            return;
        }

        // Original search/filter logic
        String searchQuery = request.getParameter("q") != null ? request.getParameter("q") : "";
        String platformFilter = request.getParameter("platform");
        List<Game> filteredGames = dbHandler.searchGames(searchQuery, platformFilter);
        response.getWriter().print(gson.toJson(filteredGames));
    }

    /**
     * Handles HTTP POST requests (e.g., Admin adding a new game).
     * **TEAM TASK:** Complete this method for the Admin Module.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().write("POST method not implemented yet for this endpoint.");
        // TODO: Implement logic to receive JSON/form data and call dbHandler.addGame() or dbHandler.updateGame()
    }
}