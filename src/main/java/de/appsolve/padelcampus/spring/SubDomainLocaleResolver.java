/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.data.CustomerI;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author dominik
 */
@Component
public class SubDomainLocaleResolver extends AbstractLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        setLocaleIfNecessary(request);
        return (Locale) request.getSession().getAttribute(Constants.SESSION_DEFAULT_LOCALE);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException("Cannot change sub-domain locale - use a different locale resolution strategy");
    }

    @Override
    protected Locale getDefaultLocale() {
        return Constants.DEFAULT_LOCALE;
    }

    private void setLocaleIfNecessary(HttpServletRequest request) {
        if (request.getSession().getAttribute(Constants.SESSION_DEFAULT_LOCALE) == null) {
            Locale locale = null;
            CustomerI customer = (CustomerI) request.getSession().getAttribute(Constants.SESSION_CUSTOMER);
            if (customer != null) {
                locale = StringUtils.parseLocaleString(customer.getDefaultLanguage());
                //if the customer supports multiple languages, try to get the language from the subdomain
                if (customer.getSupportedLanguages().size() > 1) {
                    String serverName = request.getServerName();
                    int indexOfDot = serverName.indexOf('.');
                    if (indexOfDot > 0) {
                        String leftMostSubdomain = serverName.substring(0, indexOfDot);
                        if (customer.getSupportedLanguages().contains(leftMostSubdomain)) {
                            locale = StringUtils.parseLocaleString(leftMostSubdomain);
                        }
                    }
                }
            }

            //fallback to default locale
            if (locale == null) {
                locale = getDefaultLocale();
            }
            request.getSession().setAttribute(Constants.SESSION_DEFAULT_LOCALE, locale);
        }
    }
}