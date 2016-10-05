/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import static de.appsolve.padelcampus.constants.Constants.COOKIE_LOGIN_TOKEN;
import de.appsolve.padelcampus.db.dao.LoginCookieDAOI;
import de.appsolve.padelcampus.db.model.LoginCookie;
import de.appsolve.padelcampus.db.model.Player;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class LoginUtil {
    
    private static final Integer ONE_YEAR_SECONDS = 60*60*24*365;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    LoginCookieDAOI loginCookieDAO;

    public void updateLoginCookie(HttpServletRequest request, HttpServletResponse response) {
        Player player = sessionUtil.getUser(request);
        if (player != null){
            UUID cookieUUID = UUID.randomUUID();
            UUID cookieValue = UUID.randomUUID();
            String cookieValueHash = BCrypt.hashpw(cookieValue.toString(), BCrypt.gensalt());
            LoginCookie loginCookie = new LoginCookie();
            loginCookie.setUUID(cookieUUID.toString());
            loginCookie.setPlayerUUID(player.getUUID());
            loginCookie.setLoginCookieHash(cookieValueHash);
            loginCookie.setValidUntil(new LocalDate().plusYears(1));
            loginCookieDAO.saveOrUpdate(loginCookie);
            Cookie cookie = new Cookie(COOKIE_LOGIN_TOKEN, cookieUUID.toString()+":"+cookieValue.toString());
            cookie.setDomain(request.getServerName());
            cookie.setMaxAge(ONE_YEAR_SECONDS);
            response.addCookie(cookie);
        }
    }

    public void deleteLoginCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals(COOKIE_LOGIN_TOKEN)){
                    if (cookie.getValue() != null && cookie.getValue().split(":").length == 2){
                        LoginCookie loginCookie = loginCookieDAO.findByUUID(cookie.getValue().split(":")[0]);
                        if (loginCookie != null){
                            loginCookieDAO.deleteById(loginCookie.getId());
                            break;
                        }
                    }
                }
            }
        }
        deleteCookie(request, response, null);
        deleteCookie(request, response, "/page");
        deleteCookie(request, response, "/admin");
        deleteCookie(request, response, "/login");
        Cookie cookie = new Cookie(COOKIE_LOGIN_TOKEN, null);
        cookie.setDomain(request.getServerName());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public LoginCookie isValidLoginCookie(String uuid, String loginCookieRandomValue) {
        LoginCookie loginCookie = loginCookieDAO.findByUUID(uuid);
        if (loginCookie == null){
            return null;
        }
        if (BCrypt.checkpw(loginCookieRandomValue, loginCookie.getLoginCookieHash())){
            return loginCookie;
        }
        return null;
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, String path) {
        Cookie cookie = new Cookie(COOKIE_LOGIN_TOKEN, null);
        cookie.setDomain(request.getServerName());
        cookie.setMaxAge(0);
        if (!StringUtils.isEmpty(path)){
            cookie.setPath(path);
        }
        response.addCookie(cookie);
    }
}
