/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import static de.appsolve.padelcampus.constants.Constants.COOKIE_LOGIN_TOKEN;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Player;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class LoginUtil {
    
    @Autowired
    PlayerDAOI playerDAO;

    public void updateLoginCookie(Player player, HttpServletRequest request, HttpServletResponse response) {
        UUID random = UUID.randomUUID();
        String randomHash = BCrypt.hashpw(random.toString(), BCrypt.gensalt());
        player.setLoginCookieHash(randomHash);
        playerDAO.saveOrUpdate(player);
        Cookie cookie = new Cookie(COOKIE_LOGIN_TOKEN, player.getUUID()+":"+random.toString());
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
    }

    public void deleteLoginCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_LOGIN_TOKEN, null);
        cookie.setDomain(request.getServerName());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
