package src.controller;

import com.google.gson.Gson;
import src.data.DatabaseHandler;
import src.model.Cart;
import src.model.Game;
import src.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/api/cart")
public class CartServlet extends HttpServlet {

    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        Cart sessionCart = (Cart) session.getAttribute("userCart");

        Cart activeCart;

        if (loggedUser != null) {
            activeCart = loggedUser.getCart();
            if (sessionCart != null && !sessionCart.getItems().isEmpty()) {
                sessionCart.getItems().forEach(item -> activeCart.addGame(item.getGame(), item.getQuantity()));
                session.removeAttribute("userCart");
                DatabaseHandler.saveUsers();
            }
        } else {
            if (sessionCart == null) {
                sessionCart = new Cart();
                session.setAttribute("userCart", sessionCart);
            }
            activeCart = sessionCart;
        }

        activeCart.updateTotal();
        response.getWriter().println(gson.toJson(activeCart));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String gameId = request.getParameter("gameId");
        String action = request.getParameter("action");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        Cart activeCart;
        if (loggedUser != null) {
            activeCart = loggedUser.getCart();
        } else {
            activeCart = (Cart) session.getAttribute("userCart");
            if (activeCart == null) {
                activeCart = new Cart();
                session.setAttribute("userCart", activeCart);
            }
        }

        if (gameId != null) {
            if ("remove".equals(action)) {
                activeCart.removeGame(gameId);
            } else {
                Optional<Game> game = dbHandler.getGameById(gameId);
                if (game.isPresent()) {
                    activeCart.addGame(game.get(), 1);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }

            // CRITICAL: If logged in, save the change to the persistent database
            if (loggedUser != null) {
                DatabaseHandler.saveUsers();
            }

            response.getWriter().write("{\"status\":\"success\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
