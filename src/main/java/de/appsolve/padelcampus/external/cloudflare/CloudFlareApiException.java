/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.cloudflare;

import de.appsolve.padelcampus.external.cloudflare.model.CloudFlareApiResponse;

/**
 * @author dominik
 */
public class CloudFlareApiException extends Exception {

    public CloudFlareApiException(CloudFlareApiResponse response) {
        super("CloudFlare API returned invalid response: " + response.getErrors());
    }
}
