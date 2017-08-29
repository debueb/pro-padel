/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.openshift;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author dominik
 */
public class OpenshiftApiRequestInterceptor implements ClientHttpRequestInterceptor {

    private String openshiftUsername;
    private String openshiftPassword;

    public OpenshiftApiRequestInterceptor(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Missing OPENSHIFT_USERNAME and/or OPENSHIFT_PASSWORD");
        }
        this.openshiftUsername = username;
        this.openshiftPassword = password;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        String auth = openshiftUsername + ":" + openshiftPassword;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        headers.add(HttpHeaders.AUTHORIZATION, new String(encodedAuth, StandardCharsets.UTF_8));
        headers.add("Content-Type", "application/json, version=1.7");
        return execution.execute(request, body);
    }
}
