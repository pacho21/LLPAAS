package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jpa.Users;
import jpa.UsersJpaController;
import tools.CookieControl;

/**
 *
 * @author admin
 */
public class Login extends HttpServlet {

    EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
    UsersJpaController uc = new UsersJpaController(emf);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Users u = uc.findUserByUsername(req.getParameter("username"));

        if (u == null) {
            //create the error message JSON type
            Map<String, String> emess = new HashMap<>();
            emess.put("error", "This username was not found");

            Gson gson = new GsonBuilder().create();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            PrintWriter pw = resp.getWriter();
            pw.println(gson.toJson(emess));
        }
    }

}
