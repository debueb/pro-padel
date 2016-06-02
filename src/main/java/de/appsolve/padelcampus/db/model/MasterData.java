/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class MasterData extends CustomerEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.companyName}")
    private String companyName;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.street}")
    private String street;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.zip}")
    private String zip;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.city}")
    private String city;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.iban}")
    private String iban;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.bic}")
    private String bic;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.institute}")
    private String institute;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.companyCity}")
    private String companyCity;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.hrb}")
    private String hrb;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.taxNumber}")
    private String taxNumber;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.tradeId}")
    private String tradeId;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.ceo}")
    private String ceo;
    
    @Column
    @NotEmpty(message = "{NotEmpty.MasterData.email}")
    private String email;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getHrb() {
        return hrb;
    }

    public void setHrb(String hrb) {
        this.hrb = hrb;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}