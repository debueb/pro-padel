/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Dominik
 */
public class XSSFilter implements Filter {

    private static final Logger log = Logger.getLogger(XSSFilter.class);

    private Pattern excludePattern;

    public void setExcludePattern(Pattern pattern) {
        this.excludePattern = pattern;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
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
