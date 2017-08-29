/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.reporting;

import com.bugsnag.Bugsnag;
import jersey.repackaged.com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author dominik
 */
@Component
public class ErrorReporter {

    private static final Pattern IGNORED_USER_AGENT_PATTERN = Pattern.compile(".*(tinfoilsecurity|Googlebot|bingbot|AhrefsBot|facebookexternalhit|seoscanners).*");
    private static final Set<String> IGNORED_EXCEPTION_CLASS_NAMES = Sets.newHashSet("ClientAbortException");

    @Autowired
    Bugsnag bugsnag;

    public void notify(Throwable ex) {
        boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("jdwp");
        if (!isDebug) {
            if (ex != null && !IGNORED_EXCEPTION_CLASS_NAMES.contains(ex.getClass().getSimpleName())) {
                if (ex.getCause() == null || !IGNORED_EXCEPTION_CLASS_NAMES.contains(ex.getCause().getClass().getSimpleName())) {
                    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                    //log all errors when we don't have a request context, e.g. for scheduled tasks etc
                    if (attr == null || attr.getRequest() == null) {
                        bugsnag.notify(ex);
                    } else {
                        //if we have a request context, ignore bot requests
                        String userAgent = attr.getRequest().getHeader(HttpHeaders.USER_AGENT);
                        if (!StringUtils.isEmpty(userAgent) && !IGNORED_USER_AGENT_PATTERN.matcher(userAgent).matches()) {
                            bugsnag.notify(ex);
                        }
                    }
                }
            }
        }
    }
}

