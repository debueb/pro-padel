/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.SystemException;

/**
 * @author dominik
 * <p>
 * this class adds an additional header to http redirects so that client side javascript knows how to update the url
 * <p>
 * since client side javascript does not return the header of the first request we have to temporarily store the header
 * in the session until the redirected view is called
 */
public class RedirectInterceptor extends HandlerInterceptorAdapter {

    private final static String REDIRECT_PREFIX = "redirect:";
    private final static String REDIRECT_HEADER = "RedirectURL";

    @Override
    public final void postHandle(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Object handler,
                                 ModelAndView modelAndView) throws SystemException {
        String redirectHeader = (String) request.getSession().getAttribute(REDIRECT_HEADER);
        if (!StringUtils.isEmpty(redirectHeader)) {
            response.addHeader(REDIRECT_HEADER, redirectHeader);
            request.getSession().removeAttribute(REDIRECT_HEADER);
        } else if (modelAndView != null) {
            String viewName = null;
            if (modelAndView.getViewName() != null && modelAndView.getViewName().startsWith(REDIRECT_PREFIX)) {
                viewName = modelAndView.getViewName().substring(REDIRECT_PREFIX.length());
            } else if (modelAndView.getView() != null && modelAndView.getView() instanceof RedirectView) {
                viewName = modelAndView.getViewName();
            }
            if (!StringUtils.isEmpty(viewName)) {
                request.getSession().setAttribute(REDIRECT_HEADER, viewName);
            }
        }
    }
}
