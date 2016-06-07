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
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.CompanyLogoUtil;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("loginFilter")
public class LoginFilter implements Filter {
    
    private static final Logger LOG = Logger.getLogger(LoginFilter.class);

    @Autowired
    CustomerDAOI customerDAO;
    
    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    CompanyLogoUtil companyLogoUtil;
    
    @Autowired
    ModuleUtil moduleUtil;
    
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
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            
            if (httpRequest.getRequestURI().startsWith(PATH_START_PAGE)){
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
                    httpResponse.sendRedirect(PATH_START_PAGE);
                    return;
                }
                sessionUtil.setCustomer(httpRequest, customer);
            }
            
            Player user = sessionUtil.getUser(httpRequest);
            if (user == null) {
                Cookie[] cookies = httpRequest.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        LOG.info(cookie.getDomain()+"=="+httpRequest.getServerName()+"="+cookie.getDomain() != null && cookie.getDomain().equalsIgnoreCase(httpRequest.getServerName()));
                        if (cookie.getDomain() != null && cookie.getDomain().equalsIgnoreCase(httpRequest.getServerName())){
                            if (cookie.getName().equals(COOKIE_LOGIN_TOKEN)) {
                                Player player = playerDAO.findByUUID(cookie.getValue());
                                sessionUtil.setUser(httpRequest, player);
                                break;
                            }
                        }
                    }
                }
            }
            
            companyLogoUtil.initModules(httpRequest);
            
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
