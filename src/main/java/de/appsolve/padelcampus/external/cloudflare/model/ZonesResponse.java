/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.cloudflare.model;

import java.util.List;

/**
 *
 * @author dominik
 */
public class ZonesResponse extends CloudFlareApiResponse{
    
    private List<Zone> result;

    public List<Zone> getResult() {
        return result;
    }

    public void setResult(List<Zone> result) {
        this.result = result;
    }
}
