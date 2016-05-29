/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.openshift;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 *
 * @author dominik
 */
public class OpenshiftApiRequestInterceptor implements ClientHttpRequestInterceptor{
    
    private static final String OPENSHIFT_USERNAME = "d.wisskirchen@gmail.com";
    private static final String OPENSHIFT_PASSWORD = "Gukn6$PanzQuve";
    
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        String auth = OPENSHIFT_USERNAME + ":" + OPENSHIFT_PASSWORD;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        headers.add(HttpHeaders.AUTHORIZATION, new String(encodedAuth));
        headers.add("Content-Type", "application/json, version=1.7");
        return execution.execute(request, body);
    }
}
