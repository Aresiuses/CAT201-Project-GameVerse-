package src.controller;

import src.data.DatabaseHandler;
import src.model.User;
import src.model.PurchaseRecord;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/history")
public class PurchaseHistoryServlet extends HttpServlet {

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

        // Fetch user-specific records from static memory via handler
        List<PurchaseRecord> records = DatabaseHandler.getHistoryForUser(user.getUserId());

        // Reverse to show newest first
        java.util.Collections.reverse(records);

        response.getWriter().write(gson.toJson(records));
    }
}