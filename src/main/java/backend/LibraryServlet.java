package backend;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/api/library")
public class LibraryServlet extends HttpServlet {

    private List<Game> library = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.getWriter().write(JsonHelper.gameListToJson(library));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String title = request.getParameter("title");
        String platform = request.getParameter("platform");
        double price = Double.parseDouble(request.getParameter("price"));

        Game game = new Game(UUID.randomUUID().toString(), title, platform, price);
        library.add(game);

        response.getWriter().write("Added to library");
    }
}
