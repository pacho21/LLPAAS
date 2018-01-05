package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import model.UserJpaController;
import tools.CookieControl;
import tools.PassCrypt;

/**
 *
 * @author admin
 */
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        UserJpaController uc = new UserJpaController(emf);
        CookieControl cookieController = new CookieControl();

        User u = cookieController.checkCookie(req.getCookies());

        if (u != null) {

            req.setAttribute("User", u);
            RequestDispatcher forwardTo = req.getRequestDispatcher("game.jsp");
            forwardTo.forward(req, resp);
        } else {
            RequestDispatcher forwardToLogin = req.getRequestDispatcher("Login.jsp");
            resp.setContentType("application/json");
            forwardToLogin.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> emess = new HashMap<>();
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        UserJpaController uc = new UserJpaController(emf);
        CookieControl cookieController = new CookieControl();

        Gson gson = new GsonBuilder().create();
        User u = uc.findUserByUsername(req.getParameter("username"));
        PassCrypt ps = new PassCrypt();

        if (u == null) {
            //create the error message JSON type       
            emess.put("error", "This username was not found");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            PrintWriter pw = resp.getWriter();
            pw.println(gson.toJson(emess));
        } else {
            try {
                if (u.getPassword().equals(ps.encrypt(req.getParameter("password")))) {

                    //set cookies
                    Cookie[] c = cookieController.createCookies(u.getUsername(), u.getPassword());

                    resp.addCookie(c[0]);
                    resp.addCookie(c[1]);

                    req.setAttribute("User", u);
                    RequestDispatcher forwardTo = req.getRequestDispatcher("/game.jsp");
                    forwardTo.forward(req, resp);
                } else {

                    emess.put("error", "Wrong password.");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.setContentType("application/json");
                    PrintWriter pw = resp.getWriter();
                    pw.println(gson.toJson(emess));
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                emess.put("error", "There was a problem with the server!");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("application/json");
                PrintWriter pw = resp.getWriter();
                pw.println(gson.toJson(emess));
            }
        }
    }

}
