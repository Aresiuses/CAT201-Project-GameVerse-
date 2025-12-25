package src.controller;

import src.data.DatabaseHandler;
import src.model.Game;
import src.model.User;

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

    // ================= VIEW WISHLIST =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }

        User user = (User) session.getAttribute("user");

        List<Game> wishlist = db.getUserWishlist(user.getId());
        request.setAttribute("wishlist", wishlist);

        request.getRequestDispatcher("wishlist.jsp")
               .forward(request, response);
    }

    // ================= ADD TO WISHLIST =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }

        User user = (User) session.getAttribute("user");
        String gameId = request.getParameter("gameId");

        DatabaseHandler.addGameToWishlist(user.getId(), gameId);

        response.sendRedirect("wishlist");
    }
}
