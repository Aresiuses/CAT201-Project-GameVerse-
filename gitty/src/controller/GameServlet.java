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

        // 1. Get search and filter parameters from the client
        String searchQuery = request.getParameter("q") != null ? request.getParameter("q") : "";
        String platformFilter = request.getParameter("platform");

        // 2. Call the Java Business Logic (Data Access Layer)
        List<Game> filteredGames = dbHandler.searchGames(searchQuery, platformFilter);

        // 3. Set up the HTTP response (Output)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Allows the frontend running locally to communicate with the server
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 4. Convert the Java list of Game objects into a JSON string
        String jsonResponse = gson.toJson(filteredGames);

        // 5. Write the JSON back to the client
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
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