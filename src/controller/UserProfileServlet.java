package src.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import src.data.DatabaseHandler;
import src.model.User;
import util.PasswordUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/profile")
public class UserProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("application/json");
        new Gson().toJson(user, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("{\"status\":\"error\",\"message\":\"Unauthorized\"}");
            return;
        }

        // Parse JSON Input
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
        String action = json.get("action").getAsString();

        try {
            if ("updateUsername".equals(action)) {
                String newUsername = json.get("username").getAsString();
                if (newUsername.length() < 3) throw new Exception("Username too short");
                user.setUsername(newUsername);
            }
            else if ("updatePassword".equals(action)) {
                String currentPass = json.get("currentPassword").getAsString();
                String newPass = json.get("newPassword").getAsString();

                // Verify current password first
                String hashedInput = PasswordUtil.hashPassword(currentPass);
                if (!hashedInput.equals(user.getPasswordHash())) {
                    throw new Exception("Current password incorrect");
                }

                user.setPasswordHash(PasswordUtil.hashPassword(newPass));
            }

            // Persist changes to disk
            DatabaseHandler.saveUsers();
            response.getWriter().print("{\"status\":\"success\",\"message\":\"Profile updated!\"}");

        } catch (Exception e) {
            response.setStatus(400);
            response.getWriter().print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}