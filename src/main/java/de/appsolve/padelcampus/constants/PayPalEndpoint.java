/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.constants;

/**
 *
 * @author dominik
 */
public enum PayPalEndpoint {
    
    Sandbox("https://api.sandbox.paypal.com/"),
    Production("https://api.paypal.com/");
    
    private String url;
    
    private PayPalEndpoint(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
