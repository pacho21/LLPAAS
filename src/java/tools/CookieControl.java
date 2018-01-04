/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.Cookie;
import jpa.Users;
import jpa.UsersJpaController;

/**
 *
 * @author admin
 */
public class CookieControl {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("LLPASS_TEAMWORKPU");
    UsersJpaController uc = new UsersJpaController(emf);

    /**
     * Method to check if the user exists inside the cookie array. If this
     * exists and the password exists, returns the user.
     *
     * @param cookies
     * @return the user
     */
    public Users checkCookie(Cookie cookies[]) {
        String username = null;
        String password = null;

        for (Cookie c : cookies) {
            if (c.getName().equals("usrnam")) {
                username = c.getValue();
            }
            if (c.getName().equals("passwd")) {
                password = c.getValue();
            }
        }
        if (username != null && password != null) {
            Users u = uc.findUserByUsername(username);
            
            if (u != null && u.getPassword().equals(password)) {
                return u;
            } else {
                return null;
            }

        }

        return null;
    }

    public Cookie[] createCookies(String user, String pass) {
        
        Cookie userCookie = new Cookie("usrnam", user);
        Cookie passCookie = new Cookie("passwd", pass);
        
        userCookie.setMaxAge(60 * 30);
        passCookie.setMaxAge(60 * 30);
        
        userCookie.setPath("/");
        passCookie.setPath("/");

        Cookie cookies[] = {userCookie, passCookie};

        return cookies;

    }

}
