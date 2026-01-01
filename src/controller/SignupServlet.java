package src.controller;

import src.data.DatabaseHandler;
import src.model.User;
import util.PasswordUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class SignupServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

    User user = DatabaseHandler.registerCustomer(
            request.getParameter("username"),
            request.getParameter("email"),
            PasswordUtil.hashPassword(request.getParameter("password"))
    );

    if (user == null) {
        response.sendRedirect("signup.html?error=email_exists");
        return;
    }

    request.getSession().setAttribute("loggedUser", user);
    response.sendRedirect("index.html");
}

}

