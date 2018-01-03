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

    public Users checkCookie(Cookie cookies[]) {
        String username = null;
        String password = null;

        for (Cookie c : cookies) {
            if (c.getName().equals("username")) {
                username = c.getValue();
            }
            if (c.getName().equals("password")) {
                password = c.getValue();
            }
        }
        if (username != null && password != null) {
            Users u = uc.findUserByUsername(username);
            if(u!=null&&u.getPassword().equals(password)){
                return u;
            }else{
                return null;
            }
            
        }

        return null;
    }

}
