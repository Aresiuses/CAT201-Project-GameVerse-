package controller;

import data.DatabaseHandler;
import model.User;
import util.PasswordUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = DatabaseHandler.authenticateUser(
                email,
                PasswordUtil.hashPassword(password)
        );

        if (user == null) {
            response.sendRedirect("login.html?error=invalid");
            return;
        }

        request.getSession().setAttribute("loggedUser", user);
        response.sendRedirect("index.html");
    }
}
