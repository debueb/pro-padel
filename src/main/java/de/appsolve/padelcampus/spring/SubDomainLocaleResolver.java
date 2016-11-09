/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.constants.Constants;
import static de.appsolve.padelcampus.constants.Constants.SESSION_DEFAULT_LOCALE;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

/**
 *
 * @author dominik
 */
public class SubDomainLocaleResolver extends AbstractLocaleResolver {
    
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        setLocaleIfNecessary(request);
        return (Locale) request.getSession(true).getAttribute(SESSION_DEFAULT_LOCALE);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException("Cannot change sub-domain locale - use a different locale resolution strategy");
    }

    private void setLocaleIfNecessary(HttpServletRequest request) {
        if (request.getSession(true).getAttribute(SESSION_DEFAULT_LOCALE) == null){
            Locale locale = null;
            String serverName = request.getServerName();
            int indexOfDot = serverName.indexOf('.');
            if (indexOfDot > 0){
                String leftMostSubdomain = serverName.substring(0, indexOfDot);
                if (Constants.VALID_LANGUAGES.contains(leftMostSubdomain)){
                    locale = StringUtils.parseLocaleString(leftMostSubdomain);
                }
            }
            if (locale == null) {
                locale = determineDefaultLocale(request);
            }
            request.getSession(true).setAttribute(SESSION_DEFAULT_LOCALE, locale);
        }
    }
    
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        Locale defaultLocale = getDefaultLocale();
        if (defaultLocale == null) {
            defaultLocale = request.getLocale();
        }
        return defaultLocale;
    }
}