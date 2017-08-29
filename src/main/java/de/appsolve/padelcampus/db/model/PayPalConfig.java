/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.PayPalEndpoint;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author dominik
 */
@Entity
public class PayPalConfig extends CustomerEntity {

    private static final long serialVersionUID = 1L;

    @Column
    private Boolean active;

    @Column
    @Enumerated(EnumType.STRING)
    private PayPalEndpoint payPalEndpoint;

    @Column
    @NotEmpty(message = "{NotEmpty.clientId}")
    private String clientId;

    @Column
    @NotEmpty(message = "{NotEmpty.clientSecret}")
    private String clientSecret;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public PayPalEndpoint getPayPalEndpoint() {
        return payPalEndpoint;
    }

    public void setPayPalEndpoint(PayPalEndpoint payPalEndpoint) {
        this.payPalEndpoint = payPalEndpoint;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
