package src.controller;

import src.model.Game;
import src.model.Library;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/library")
public class LibraryServlet extends HttpServlet {

    private final Library library = new Library(); // Single library instance (for demo)

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        StringBuilder html = new StringBuilder("<h1>Game Library</h1><ul>");
        for (Game game : library.getAllGames()) {
            html.append("<li>")
                    .append(game.getTitle())
                    .append(" - $")
                    .append(game.getPrice())
                    .append(" (")
                    .append(game.getPlatform())
                    .append(")")
                    .append("</li>");
        }
        html.append("</ul>");
        response.getWriter().println(html.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String platform = request.getParameter("platform");
        double price = Double.parseDouble(request.getParameter("price"));

        Game game = new Game(id, title, platform, price);
        library.addGame(game);

        response.setContentType("text/plain");
        response.getWriter().println("Added " + title + " to library!");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        library.getAllGames().removeIf(game -> game.getId().equals(id));

        response.setContentType("text/plain");
        response.getWriter().println("Removed game with ID " + id + " from library.");
    }
}
