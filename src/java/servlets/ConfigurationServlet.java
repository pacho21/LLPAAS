/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ConfigurationJpaController;
import model.Dificulty;
import model.DificultyJpaController;
import model.User;
import model.UserJpaController;
import tools.CookieControl;

/**
 *
 * @author admin
 */
public class ConfigurationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
            UserJpaController uc = new UserJpaController(emf);
            CookieControl cook = new CookieControl();
            User u = cook.checkCookie(request.getCookies());

            if (u == null) {
                Map<String, String> emess = new HashMap<>();
                emess.put("error", "User not found");

                Gson gson = new GsonBuilder().create();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(emess));
            } else {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                if (!u.getConfigurationList().isEmpty()) {
                    pw.println(gson.toJson(u.getConfigurationList()));
                } else {
                    Map<String, String> emess = new HashMap<>();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    emess.put("error", "Configurations not found!");
                    pw.println(emess);
                }
            }
        } catch (Exception e) {
            Map<String, String> emess = new HashMap<>();
            emess.put("error", "Server error");

            Gson gson = new GsonBuilder().create();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(emess));

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
            UserJpaController uc = new UserJpaController(emf);
            ConfigurationJpaController cc = new ConfigurationJpaController(emf);
            CookieControl cook = new CookieControl();
            User u = cook.checkCookie(request.getCookies());
            if (u == null) {
                Map<String, String> emess = new HashMap<>();
                emess.put("error", "User not found");

                Gson gson = new GsonBuilder().create();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(emess));
            } else {

                model.DificultyJpaController dc = new DificultyJpaController(emf);
                Dificulty d = dc.findDificulty(request.getParameter("cfdificulty"));
                model.Configuration conf = new model.Configuration();
                System.out.println("1st");
                conf.setCfName(request.getParameter("configname"));
                conf.setCfDificulty(d);
                conf.setCfMoon(request.getParameter("cfmoon"));
                conf.setCfSpaceship(request.getParameter("cfspaceship"));
                conf.setUsId(u);
                System.out.println("hola");
                cc.create(conf);

                Map<String, String> mess = new HashMap<>();
                mess.put("mess", "Configuration added");

                Gson gson = new GsonBuilder().create();

                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(mess));

            }

        } catch (Exception e) {
            Map<String, String> emess = new HashMap<>();
            emess.put("error", "Server error");

            Gson gson = new GsonBuilder().create();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(emess));

        }
    }

}
