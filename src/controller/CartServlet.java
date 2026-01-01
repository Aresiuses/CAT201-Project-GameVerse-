package src.controller;

import src.model.Cart;
import src.model.Game;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/cart")
public class CartServlet extends HttpServlet {

    private final Cart cart = new Cart(); // Single cart instance (for demo)

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        StringBuilder html = new StringBuilder("<h1>Your Cart</h1><ul>");
        for (Game game : cart.getItems()) {
            html.append("<li>")
                    .append(game.getTitle())
                    .append(" - $")
                    .append(game.getPrice())
                    .append("</li>");
        }
        html.append("</ul>");
        html.append("<p>Total: $").append(cart.getTotalPrice()).append("</p>");
        response.getWriter().println(html.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Add a game via POST parameters
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String platform = request.getParameter("platform");
        double price = Double.parseDouble(request.getParameter("price"));

        Game game = new Game(id, title, platform, price);
        cart.add(game);

        response.setContentType("text/plain");
        response.getWriter().println("Added " + title + " to cart!");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Optional: Remove a game from the cart by ID
        String id = request.getParameter("id");
        cart.getItems().removeIf(game -> game.getId().equals(id));

        response.setContentType("text/plain");
        response.getWriter().println("Removed game with ID " + id + " from cart.");
    }
}
