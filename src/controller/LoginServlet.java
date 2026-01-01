package src.controller;

import src.data.DatabaseHandler;
import src.model.User;
import util.PasswordUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = DatabaseHandler.authenticate(
                email,
                PasswordUtil.hashPassword(password)
        );

        if (user == null) {
            response.sendRedirect("login.html?error=invalid");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("loggedUser", user);

        response.sendRedirect("index.html");
    }
}
