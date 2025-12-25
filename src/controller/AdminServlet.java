package src.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import src.data.DatabaseHandler;
import src.model.Game;
import src.model.User;
import src.service.GameDataAPIService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/api/admin")
public class AdminServlet extends HttpServlet {
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final GameDataAPIService apiService = new GameDataAPIService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        // --- 1. Authentication  ---
        HttpSession session = req.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser == null) {
            sendError(response, "Unauthorized: Please log in to access admin functionality.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (loggedUser.getRole() != User.Role.ADMIN) {
            sendError(response, "Forbidden: User does not have administrative privilages.", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        System.out.println("AdminServlet: Admin User '" + loggedUser.getUsername() + "' has been logged in.");

        // --- 2. Read Input Data (Game Title) ---
        // Read the JSON body sent by the frontend (admin.html)
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            sendError(response, "Error reading request body: " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //Parsing JSON to get game title
        JsonObject inputData = gson.fromJson(sb.toString(), JsonObject.class);
        String gameTitle = inputData.title;
        double price = inputData.price;
        String platform = inputData.platform;

        if (gameTitle == null || gameTitle.trim().isEmpty()) {
            sendError(response, "Missing game title in request.", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // --- 3. Java Processing: Fetch API Data ---
        System.out.println("AdminServlet: Fetching metadata for: " + gameTitle);
        Optional<Game> apiDetails = apiService.fetchGameMetadata(gameTitle);

        if(!apiDetails.isPresent()) {
            sendError(response, "Could not fetch metadata from IGDB for: " + gameTitle + ". Check console for API errors.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // --- 4. Combine and Finalize Game Object ---
        Game newGame = apiDetails.get();
        newGame.setPlatform(platform);
        newGame.setPrice(price);

        // --- 5. Java Processing: Persistence ---
        dbHandler.addGame(newGame);
        System.out.println("AdminServlet: Successfully added new game: " + gameTitle + " to database.");

        // --- 6. Send Success Response ---
        String successJson = gson.toJson(new ResponseMessage("Game added successfully!", newGame.getTitle() + " has been added to the catalog"));
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(successJson);

    }

    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.getWriter().print(gson.toJson(new ResponseMessage("Error", message)));
        System.err.println("AdminServlet Error: " + message);
    }

    private static class ResponseMessage {
        String status;
        String message;
        public ResponseMessage(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    private static class JsonObject {
        String title;
        String platform;
        double price;
    }
}
