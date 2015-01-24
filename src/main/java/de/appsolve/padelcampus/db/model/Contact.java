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
public class Contact extends BaseEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @Email(message = "{Email}")
    @NotEmpty(message = "{NotEmpty.email}")
    private String emailAddress;
    
    @Column
    @NotEmpty(message = "{NotEmpty.name}")
    private String emailDisplayName;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailDisplayName() {
        return emailDisplayName;
    }

    public void setEmailDisplayName(String emailDisplayName) {
        this.emailDisplayName = emailDisplayName;
    }
    
    @Override
    public String getDisplayName() {
        return emailDisplayName + " ("+emailAddress+")";
    }
}
