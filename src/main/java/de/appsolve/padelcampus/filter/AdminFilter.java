/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.filter;

import static de.appsolve.padelcampus.constants.Constants.SESSION_LOGIN_REDIRECT_PATH;
import de.appsolve.padelcampus.constants.Privilege;
import de.appsolve.padelcampus.db.dao.AdminGroupDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.io.IOException;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("adminFilter")
public class AdminFilter implements Filter {

    @Autowired
    AdminGroupDAOI adminGroupDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
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
        throws IOException, ServletException
    {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            Player player = sessionUtil.getUser(httpRequest);
            if (player==null){
                sessionUtil.setLoginRedirectPath(httpRequest, httpRequest.getRequestURI());
                httpResponse.sendRedirect(httpResponse.encodeRedirectURL("/login"));
                return;
            }
            Set<Privilege> privileges = sessionUtil.getPrivileges(httpRequest);
            String pathInfo = httpRequest.getServletPath();
            for (Privilege privilege: privileges){
                if (privilege.getPathPattern().matcher(pathInfo).matches()){
                    chain.doFilter(request, response);
                    return;
                }
            }
            
            httpResponse.sendError(HttpStatus.SC_FORBIDDEN);
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        //
    }
}