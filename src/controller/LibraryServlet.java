package src.controller;

import src.data.DatabaseHandler;
import src.model.Game;
import src.model.User;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/api/library")
public class LibraryServlet extends HttpServlet {

    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        //1. Check Authentication
        HttpSession session = request.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if  (loggedUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //2. Fetch owned game with DBHandler
        List<String> ownedIds = loggedUser.getOwnedGameIds();
        List<Game> libraryGames = new ArrayList<>();

        for (String id : ownedIds) {
            dbHandler.getGameById(id).ifPresent(libraryGames::add);
        }

        //3. Return as JSON
        response.getWriter().println(gson.toJson(libraryGames));
    }

}
