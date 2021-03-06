/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author dominik
 */
@Entity
public class PayMillConfig extends CustomerEntity {

    private static final long serialVersionUID = 1L;

    @Column
    private Boolean enableDirectDebit;

    @Column
    private Boolean enableCreditCard;

    @Column
    @NotEmpty(message = "{NotEmpty.privateApiKey}")
    private String privateApiKey;

    @Column
    @NotEmpty(message = "{NotEmpty.publicApiKey}")
    private String publicApiKey;

    public Boolean getEnableDirectDebit() {
        return enableDirectDebit;
    }

    public void setEnableDirectDebit(Boolean enableDirectDebit) {
        this.enableDirectDebit = enableDirectDebit;
    }

    public Boolean getEnableCreditCard() {
        return enableCreditCard;
    }

    public void setEnableCreditCard(Boolean enableCreditCard) {
        this.enableCreditCard = enableCreditCard;
    }

    public String getPrivateApiKey() {
        return privateApiKey;
    }

    public void setPrivateApiKey(String privateApiKey) {
        this.privateApiKey = privateApiKey;
    }

    public String getPublicApiKey() {
        return publicApiKey;
    }

    public void setPublicApiKey(String publicApiKey) {
        this.publicApiKey = publicApiKey;
    }
}
