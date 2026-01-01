package src.controller;

import src.data.DatabaseHandler;
import src.model.Game;
import src.model.User;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/wishlist")
public class WishlistController extends HttpServlet {

    private DatabaseHandler db;

    @Override
    public void init() {
        db = new DatabaseHandler();
    }

    // ================= VIEW WISHLIST (JSON) =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = (User) session.getAttribute("user");

        List<Game> wishlist = db.getUserWishlist(user.getId());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new Gson().toJson(wishlist, response.getWriter());
    }

    // ================= ADD TO WISHLIST =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = (User) session.getAttribute("user");
        String gameId = request.getParameter("gameId");

        boolean success = DatabaseHandler.addGameToWishlist(user.getId(), gameId);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\": " + success + "}");
    }
}
