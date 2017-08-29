/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.cloudflare;

import org.apache.http.entity.ContentType;
import org.jadira.usertype.spi.utils.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author dominik
 */
public class CloudFlareApiRequestInterceptor implements ClientHttpRequestInterceptor {

    private final String cloudFlareApiEmail;

    private final String cloudFlareApiKey;

    public CloudFlareApiRequestInterceptor(String email, String key) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Missing CLOUDFLARE_API_KEY and/or CLOUDFLARE_API_EMAIL");
        }
        this.cloudFlareApiEmail = email;
        this.cloudFlareApiKey = key;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add("X-Auth-Key", cloudFlareApiKey);
        headers.add("X-Auth-Email", cloudFlareApiEmail);
        headers.add("Content-Type", ContentType.APPLICATION_JSON.toString());
        return execution.execute(request, body);
    }

}
