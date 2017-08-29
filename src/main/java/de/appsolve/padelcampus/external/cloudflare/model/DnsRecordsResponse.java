/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.external.cloudflare.model;

import java.util.List;

/**
 * @author dominik
 */
public class DnsRecordsResponse extends CloudFlareApiResponse {

    private List<DnsRecord> result;

    public List<DnsRecord> getResult() {
        return result;
    }

    public void setResult(List<DnsRecord> result) {
        this.result = result;
    }
}
