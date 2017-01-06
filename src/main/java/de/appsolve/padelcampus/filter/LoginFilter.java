/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.filter;

import static de.appsolve.padelcampus.constants.Constants.COOKIE_LOGIN_TOKEN;
import de.appsolve.padelcampus.data.CustomerI;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.LoginCookie;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.LoginUtil;
import de.appsolve.padelcampus.utils.ModuleUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Component("loginFilter")
public class LoginFilter implements Filter {
    
    @Autowired
    CustomerDAOI customerDAO;
    
    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    ModuleUtil moduleUtil;
    
    @Autowired
    LoginUtil loginUtil;
    
    private static final String PATH_START_PAGE = "/pro";

    /**
     * @param config
     * @throws javax.servlet.ServletException
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        //
    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            
            if (httpRequest.getRequestURI().startsWith(PATH_START_PAGE)){
                sessionUtil.setCustomer(httpRequest, null);
                chain.doFilter(request, response);
                return;
            }
            
            CustomerI customer = sessionUtil.getCustomer(httpRequest);
            if (customer == null){
                String hostHeader = httpRequest.getHeader("host");
                if (!StringUtils.isEmpty(hostHeader)){
                    String[] hostHeaderSplit = hostHeader.split(":");
                    customer = customerDAO.findByDomainName(hostHeaderSplit[0]);
                } 
                if (customer == null){
                    String url = "";
                    String serverName = httpRequest.getServerName();
                    if (!StringUtils.isEmpty(serverName)){
                        String[] domainParts = serverName.split("\\.");
                        if (domainParts.length>2){
                            url = httpRequest.getScheme()+"://"+domainParts[domainParts.length-2]+"."+domainParts[domainParts.length-1];
                            if (httpRequest.getScheme().equals("http") && httpRequest.getServerPort() != 80){
                                url += ":"+httpRequest.getServerPort();
                            }
                        }
                    }
                    url += PATH_START_PAGE;
                    httpResponse.setStatus(HttpStatus.PERMANENT_REDIRECT.value());
                    httpResponse.sendRedirect(url);
                    return;
                }
                sessionUtil.setCustomer(httpRequest, customer);
            }
            
            //login user in case of valid login cookie
            Player user = sessionUtil.getUser(httpRequest);
            if (user == null) {
                Cookie[] cookies = httpRequest.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName()!=null && cookie.getName().equals(COOKIE_LOGIN_TOKEN)) {
                            String cookieValue = cookie.getValue();
                            if (StringUtils.isEmpty(cookieValue)){
                                loginUtil.deleteLoginCookie(httpRequest, httpResponse);
                            } else {
                                String[] cookieValueSplit = cookieValue.split(":");
                                if (cookieValueSplit.length != 2){
                                    loginUtil.deleteLoginCookie(httpRequest, httpResponse);
                                } else {
                                    String uuid = cookieValueSplit[0];
                                    String loginCookieRandomValue = cookieValueSplit[1];
                                    LoginCookie loginCookie = loginUtil.isValidLoginCookie(uuid, loginCookieRandomValue);
                                    if (loginCookie == null){
                                        loginUtil.deleteLoginCookie(httpRequest, httpResponse);
                                    } else {
                                        Player player = playerDAO.findByUUID(loginCookie.getPlayerUUID());
                                        if (player == null){
                                            loginUtil.deleteLoginCookie(httpRequest, httpResponse);
                                        } else {
                                            //log user in
                                            sessionUtil.setUser(httpRequest, player);

                                            //update loginCookieHash
                                            loginUtil.updateLoginCookie(httpRequest, httpResponse);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            moduleUtil.initModules(httpRequest);
        } 
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        //
    }
}
