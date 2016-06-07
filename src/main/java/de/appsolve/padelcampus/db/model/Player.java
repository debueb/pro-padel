/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.constants.SkillLevel;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.utils.CryptUtil;
import de.appsolve.padelcampus.validation.constraints.Phone;
import java.util.Collections;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author dominik
 */
@Entity
@DiscriminatorValue("Player")
public class Player extends Participant implements EmailContact{
    
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
    private Boolean allowEmailContact;
    
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
    private Boolean salted;
    
    @Column
    @JsonIgnore
    private String passwordResetUUID;
    
    @Column
    @JsonIgnore
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime passwordResetExpiryDate;
    
    @Transient
    private MultipartFile profileImageMultipartFile;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Image profileImage;
    
    @Column
    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;
    
    @Column
    private Boolean enableMatchNotifications;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<SkillLevel> notificationSkillLevels;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column
    private String loginCookieHash;
    
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

    public Boolean getAllowEmailContact() {
        return allowEmailContact == null ? Boolean.FALSE : allowEmailContact;
    }

    public void setAllowEmailContact(Boolean allowEmailContact) {
        this.allowEmailContact = allowEmailContact;
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

    public Boolean getSalted() {
        return salted == null ? Boolean.FALSE : salted;
    }

    public void setSalted(Boolean salted) {
        this.salted = salted;
    }

    public String getPasswordResetUUID() {
        return passwordResetUUID;
    }

    public void setPasswordResetUUID(String passwordResetUUID) {
        this.passwordResetUUID = passwordResetUUID;
    }

    public DateTime getPasswordResetExpiryDate() {
        return passwordResetExpiryDate;
    }

    public void setPasswordResetExpiryDate(DateTime passwordResetExpiryDate) {
        this.passwordResetExpiryDate = passwordResetExpiryDate;
    }

    public MultipartFile getProfileImageMultipartFile() {
        return profileImageMultipartFile;
    }

    public void setProfileImageMultipartFile(MultipartFile profileImageMultipartFile) {
        this.profileImageMultipartFile = profileImageMultipartFile;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public SkillLevel getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    public Boolean getEnableMatchNotifications() {
        return enableMatchNotifications == null ? Boolean.FALSE : enableMatchNotifications;
    }

    public void setEnableMatchNotifications(Boolean enableMatchNotifications) {
        this.enableMatchNotifications = enableMatchNotifications;
    }

    public Set<SkillLevel> getNotificationSkillLevels() {
        return notificationSkillLevels == null ? Collections.<SkillLevel>emptySet() : notificationSkillLevels;
    }

    public void setNotificationSkillLevels(Set<SkillLevel> notificationSkillLevels) {
        this.notificationSkillLevels = notificationSkillLevels;
    }
    
    @Override
    public String toString(){
        return firstName+" "+lastName;
    }
    
    public String getObfuscatedPhone(){
        return CryptUtil.rot47(phone);
    }
    
    public String getObfuscatedEmail(){
        return CryptUtil.rot47(email);
    }

    @Override
    public String getEmailAddress() {
        return getEmail();
    }

    @Override
    public String getEmailDisplayName() {
        return toString();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getLoginCookieHash() {
        return loginCookieHash;
    }

    public void setLoginCookieHash(String loginCookieHash) {
        this.loginCookieHash = loginCookieHash;
    }
}
