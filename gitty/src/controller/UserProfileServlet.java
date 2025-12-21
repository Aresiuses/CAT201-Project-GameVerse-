package controller;

import com.google.gson.Gson;
import model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class UserProfileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null)
                ? (User) session.getAttribute("loggedUser")
                : null;

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("application/json");
        new Gson().toJson(user, response.getWriter());
    }
}
