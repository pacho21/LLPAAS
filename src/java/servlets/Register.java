package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import model.UserJpaController;
import tools.PassCrypt;

/**
 *
 * @author admin
 */
public class Register extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> emess = new HashMap<>();
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        UserJpaController uc = new UserJpaController(emf);
        Map<String, String> mess = new HashMap<>();
        Gson gson = new GsonBuilder().create();
        PassCrypt ps = new PassCrypt();

        try {
            if (!uc.existByUsername(request.getParameter("userName"))) {
                User u = new User();
                //{username: regUser, password: regPassword, name: regName,email:regMail}

                u.setUsername(request.getParameter("username"));
                u.setRealname(request.getParameter("name"));
                try {
                    u.setPassword(ps.encrypt(request.getParameter("password")));
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
                }
                u.setGamesplayed(0);

                //set data to the user or create another constructor that gets all teh data.
                uc.create(u);

                mess.put("mess", "Registration successful! :)");

                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(mess));

            } else {

                mess.put("error", "Username already exists");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(mess));
            }
        } catch (Exception e) {

            emess.put("error", "Server error");

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(emess));

        }

    }

}
