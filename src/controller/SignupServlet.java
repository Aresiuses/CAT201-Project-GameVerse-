package controller;

import data.DatabaseHandler;
import model.User;
import util.PasswordUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class SignupServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = DatabaseHandler.registerUser(
                username,
                email,
                PasswordUtil.hashPassword(password)
        );

        if (user == null) {
            response.sendRedirect("signup.html?error=email_exists");
            return;
        }

        request.getSession().setAttribute("loggedUser", user);
        response.sendRedirect("index.html");
    }
}
