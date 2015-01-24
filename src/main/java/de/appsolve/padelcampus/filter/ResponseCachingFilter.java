/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

/**
 *
 * @author dominik
 */
public class ResponseCachingFilter extends WebContentInterceptor implements Filter{
    
    @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            try {
                this.preHandle((HttpServletRequest) request, (HttpServletResponse) response, chain);
            } catch (Exception e) {
                throw new ServletException(e);
            }
            chain.doFilter(request, response);
        }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //empty
    }

    @Override
    public void destroy() {
        //empty
    }
}
