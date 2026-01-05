package src.controller;

import src.data.DatabaseHandler;
import src.model.User;
import util.PasswordUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Hash password before sending to database
        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = DatabaseHandler.registerCustomer(
                username,
                email,
                hashedPassword
        );

        if (user == null) {
            // Email likely exists
            response.sendRedirect("signup.html?error=email_exists");
            return;
        }

        // Auto-login after successful signup
        request.getSession().setAttribute("loggedUser", user);
        response.sendRedirect("index.html");
    }
}