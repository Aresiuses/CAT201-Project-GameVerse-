package src.controller;

import src.data.DatabaseHandler;
import src.model.*;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/api/library")
public class LibraryServlet extends HttpServlet {
    private final DatabaseHandler db = new DatabaseHandler();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<Map<String, Object>> libraryView = new ArrayList<>();

        // Loop through "id:platform" strings
        for (String entry : user.getLibrary()) {
            String[] parts = entry.split(":");
            String id = parts[0];
            String purchasedPlatform = parts[1];

            db.getGameById(id).ifPresent(game -> {
                Map<String, Object> item = new HashMap<>();
                item.put("game", game);
                item.put("purchasedPlatform", purchasedPlatform);
                libraryView.add(item);
            });
        }

        response.getWriter().println(new Gson().toJson(libraryView));
    }
}