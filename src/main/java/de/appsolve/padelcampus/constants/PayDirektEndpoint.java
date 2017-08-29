/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.constants;

/**
 * @author dominik
 */
public enum PayDirektEndpoint {

    Sandbox("https://api.sandbox.paydirekt.de/api/merchantintegration/v1"),
    Production("https://api.paydirekt.de/api/merchantintegration/v1");

    private String url;

    private PayDirektEndpoint(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
