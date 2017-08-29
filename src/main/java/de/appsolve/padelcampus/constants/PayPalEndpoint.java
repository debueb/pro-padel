/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.constants;

/**
 * @author dominik
 */
public enum PayPalEndpoint {

    Sandbox("https://api.sandbox.paypal.com/", "sandbox"),
    Production("https://api.paypal.com/", "live");

    private final String url;
    private final String mode;

    private PayPalEndpoint(String url, String mode) {
        this.url = url;
        this.mode = mode;
    }

    public String getUrl() {
        return url;
    }

    public String getMode() {
        return mode;
    }
}
