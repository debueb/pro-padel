/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.appsolve.padelcampus.utils.CryptUtil;
import de.appsolve.padelcampus.validation.constraints.Phone;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
@DiscriminatorValue("Player")
public class Player extends Participant{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.firstName}")
    @JsonIgnore
    private String firstName;
    
    @Column
    @NotEmpty(message = "{NotEmpty.lastName}")
    @JsonIgnore
    private String lastName;
    
    @Column
    @NotEmpty(message = "{NotEmpty.email}")
    @Email(message = "{Email}")
    @JsonIgnore
    private String email;
    
    @Column
    private Boolean verified;
    
    @Column
    @NotEmpty(message = "{NotEmpty.phone}")
    @Phone(message = "{Phone}")
    @JsonIgnore
    private String phone;
    
    @Transient
    @JsonIgnore
    //do not validate this field using Hibernate validation annotation b/c it will throw an error despite the @Tramsient annotation in case we saveOrUpdate it
    //@NotEmpty(message = "{NotEmpty.password}")
    private String password;
    
    @Column
    @JsonIgnore
    private String passwordHash;
    
    @Column
    @JsonIgnore
    private String passwordResetUUID;
    
    @Column
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordResetExpiryDate;
    
    private String UUID;
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordResetUUID() {
        return passwordResetUUID;
    }

    public void setPasswordResetUUID(String passwordResetUUID) {
        this.passwordResetUUID = passwordResetUUID;
    }

    public Date getPasswordResetExpiryDate() {
        return passwordResetExpiryDate;
    }

    public void setPasswordResetExpiryDate(Date passwordResetExpiryDate) {
        this.passwordResetExpiryDate = passwordResetExpiryDate;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    
    @Override
    public String getDisplayName(){
        return firstName+" "+lastName;
    }
    
    @Override
    public String toString(){
        return getDisplayName();
    }
    
    public String getObfuscatedPhone(){
        return CryptUtil.rot47(phone);
    }
    
    public String getObfuscatedEmail(){
        return CryptUtil.rot47(email);
    }
}
