package src.controller;

import src.data.DatabaseHandler;
import src.model.User;
import src.model.Cart;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/checkout")
public class CheckoutServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Session expired. Please login again.\"}");
            return;
        }

        // Logic to transfer cart items to owned games list
        boolean success = DatabaseHandler.finalizePurchase(loggedUser.getUserId());

        if (success) {
            response.getWriter().write("{\"status\":\"success\", \"message\":\"Purchase completed successfully!\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Checkout failed on server.\"}");
        }
    }
}
