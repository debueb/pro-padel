/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.appsolve.padelcampus.annotations.EmailWithTld;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.constants.SkillLevel;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.validation.constraints.Phone;
import de.appsolve.padelcampus.validation.constraints.SelfValidating;
import de.appsolve.padelcampus.validation.constraints.Validatable;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author dominik
 */
@Entity
@DiscriminatorValue("Player")
@SelfValidating(message = "{FirstNameAndLastNameMustBeDifferent}")
public class Player extends Participant implements EmailContact, Validatable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column
    @JsonIgnore
    @NotEmpty(message = "{NotEmpty.firstName}")
    @Length(min = 2, max = 255, message = "{Length.firstName}")
    @Pattern(regexp = "^[^\\s][À-ÿA-z\\-\\s]*[^\\s]$", message = "{RegExp.AlphaNum}")
    private String firstName;

    @Column
    @JsonIgnore
    @NotEmpty(message = "{NotEmpty.lastName}")
    @Length(min = 2, max = 255, message = "{Length.lastName}")
    @Pattern(regexp = "^[^\\s][À-ÿA-z\\-\\s]*[^\\s]$", message = "{RegExp.AlphaNum}")
    private String lastName;

    @Column
    @NotEmpty(message = "{NotEmpty.email}")
    @EmailWithTld
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
    private Boolean enableMatchNotifications;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<SkillLevel> notificationSkillLevels;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DaySchedule> daySchedules;

    @Column
    private Boolean deleted;

    @Transient
    private Map<String, Object> substitutionData;

    @Column
    private BigDecimal balance;

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
        return getDeleted() ? Boolean.FALSE : allowEmailContact == null ? Boolean.TRUE : allowEmailContact;
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
    public String getEmailAddress() {
        return getEmail();
    }

    @Override
    public String getEmailDisplayName() {
        return getFirstName();
    }

    @Override
    public Map<String, Object> getSubstitutionData() {
        return this.substitutionData;
    }

    @Override
    public void setSubstitutionData(Map<String, Object> substitutionData) {
        this.substitutionData = substitutionData;
    }

    public BigDecimal getBalance() {
        return balance == null ? BigDecimal.ZERO : balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<DaySchedule> getDaySchedules() {
        return daySchedules;
    }

    public void setDaySchedules(Set<DaySchedule> daySchedules) {
        this.daySchedules = daySchedules;
    }

    public Boolean getDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Transient
    public boolean getGuest() {
        return StringUtils.isEmpty(getPasswordHash());
    }

    @Override
    public boolean isValid() {
        if (!StringUtils.isEmpty(getFirstName()) && !StringUtils.isEmpty(getLastName()) && getFirstName().toLowerCase(Constants.DEFAULT_LOCALE).equals(getLastName().toLowerCase(Constants.DEFAULT_LOCALE))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (getDeleted()) {
            return "Deleted Account";
        }
        return firstName + " " + lastName;
    }
}
