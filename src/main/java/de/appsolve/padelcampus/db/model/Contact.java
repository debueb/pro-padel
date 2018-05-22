/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.annotations.EmailWithTld;
import de.appsolve.padelcampus.data.EmailContact;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Map;

/**
 * @author dominik
 */
@Entity
public class Contact extends CustomerEntity implements EmailContact {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column
    @EmailWithTld
    @NotEmpty(message = "{NotEmpty.email}")
    private String emailAddress;

    @Column
    @NotEmpty(message = "{NotEmpty.name}")
    private String emailDisplayName;

    @Column
    private Boolean notifyOnContactForm;

    @Column
    private Boolean notifyOnBooking;

    @Column
    private Boolean notifyOnBookingCancellation;

    @Transient
    private Map<String, Object> substitutionData;

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String getEmailDisplayName() {
        return emailDisplayName;
    }

    @Override
    public Map<String, Object> getSubstitutionData() {
        return substitutionData;
    }

    @Override
    public void setSubstitutionData(Map<String, Object> substitutionData) {
        this.substitutionData = substitutionData;
    }

    public void setEmailDisplayName(String emailDisplayName) {
        this.emailDisplayName = emailDisplayName;
    }

    public Boolean getNotifyOnContactForm() {
        return notifyOnContactForm;
    }

    public void setNotifyOnContactForm(Boolean notifyOnContactForm) {
        this.notifyOnContactForm = notifyOnContactForm;
    }

    public Boolean getNotifyOnBooking() {
        return notifyOnBooking;
    }

    public void setNotifyOnBooking(Boolean notifyOnBooking) {
        this.notifyOnBooking = notifyOnBooking;
    }

    public Boolean getNotifyOnBookingCancellation() {
        return notifyOnBookingCancellation;
    }

    public void setNotifyOnBookingCancellation(Boolean notifyOnBookingCancellation) {
        this.notifyOnBookingCancellation = notifyOnBookingCancellation;
    }

    @Override
    public String toString() {
        return emailDisplayName + " (" + emailAddress + ")";
    }
}
