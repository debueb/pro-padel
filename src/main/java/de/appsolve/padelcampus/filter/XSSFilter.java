/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.filter;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Dominik
 */
public class XSSFilter implements Filter {
    
    private static final Logger log = Logger.getLogger(XSSFilter.class);

    private Pattern excludePattern;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludePattern = Pattern.compile(filterConfig.getInitParameter("excludePatterns"));
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (!excludePattern.matcher(httpRequest.getRequestURI()).matches()) {
                chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
