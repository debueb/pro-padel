/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.cloudflare;

import java.io.IOException;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 *
 * @author dominik
 */
public class CloudFlareApiRequestInterceptor implements ClientHttpRequestInterceptor{

    private final String CLOUDFLARE_API_EMAIL   = "d.wisskirchen@gmail.com";
    private final String CLOUDFLARE_API_KEY     = "83e79b3c1fc04c0ba4a27c7205cfff520381b";
    
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add("X-Auth-Key", CLOUDFLARE_API_KEY);
        headers.add("X-Auth-Email", CLOUDFLARE_API_EMAIL);
        headers.add("Content-Type", ContentType.APPLICATION_JSON.toString());
        return execution.execute(request, body);
    }
    
}
