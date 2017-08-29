/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.resolver;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author dominik
 */
public class PutAwareCommonsMultipartResolver extends CommonsMultipartResolver {

    private static final String MULTIPART = "multipart/";

    /**
     * Utility method that determines whether the request contains multipart
     * content.
     *
     * @param request The servlet request to be evaluated. Must be non-null.
     * @return <code>true</code> if the request is multipart; {@code false}
     * otherwise.
     * @see ServletFileUpload#isMultipartContent(HttpServletRequest)
     */
    public static final boolean isMultipartContent(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        return contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART);
    }

    @Override
    public boolean isMultipart(HttpServletRequest request) {
        return request != null && isMultipartContent(request);
    }

}
