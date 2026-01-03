package src.controller;

import src.data.DatabaseHandler;
import src.model.Game;
import src.model.User;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/wishlist")
public class WishlistServlet extends HttpServlet {

    private final DatabaseHandler db = new DatabaseHandler();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<Game> wishlist = db.getUserWishlist(user.getUserId());

        response.getWriter().write(gson.toJson(wishlist));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Please login first\"}");
            return;
        }

        String gameId = request.getParameter("gameId");
        if (gameId != null && !gameId.isEmpty()) {
            // toggleWishlistItem returns true if added, false if removed
            boolean isAdded = db.toggleWishlistItem(user.getUserId(), gameId);

            String message = isAdded ? "Game added to wishlist" : "Game removed from wishlist";
            response.getWriter().write("{\"success\": true, \"added\": " + isAdded + ", \"message\": \"" + message + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}