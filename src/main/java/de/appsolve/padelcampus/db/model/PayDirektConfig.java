/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.PayDirektEndpoint;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author dominik
 */
@Entity
public class PayDirektConfig extends CustomerEntity {

    private static final long serialVersionUID = 1L;

    @Column
    private Boolean active;

    @Column
    @Enumerated(EnumType.STRING)
    private PayDirektEndpoint payDirektEndpoint;

    @Column
    @NotEmpty(message = "{NotEmpty.apiKey}")
    private String apiKey;

    @Column
    @NotEmpty(message = "{NotEmpty.apiSecret}")
    private String apiSecret;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public PayDirektEndpoint getPayDirektEndpoint() {
        return payDirektEndpoint;
    }

    public void setPayDirektEndpoint(PayDirektEndpoint payDirektEndpoint) {
        this.payDirektEndpoint = payDirektEndpoint;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }
}
