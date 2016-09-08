/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.appsolve.padelcampus.annotations.EmailWithTld;
import de.appsolve.padelcampus.data.CustomerI;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class Customer extends BaseEntity implements CustomerI{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.name}")
    private String name;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @NotEmpty(message = "{NotEmpty.domainNames}")
    private Set<String> domainNames;

    @Column
    private String googleAnalyticsTrackingId;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Image companyLogo;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Image touchIcon;
    
    @Column(length = 8000)
    private String footerPrefix;
    
    @Column
    private String defaultEmail;
    
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Transient
    public String getDomainName(){
        if (domainNames != null){
            return domainNames.iterator().next();
        }
        return null;
    }

    public Set<String> getDomainNames() {
        return domainNames;
    }

    public void setDomainNames(Set<String> domainNames) {
        this.domainNames = domainNames;
    }
    
    @Override
    public String toString(){
        return name;
    }

    public String getGoogleAnalyticsTrackingId() {
        return googleAnalyticsTrackingId;
    }

    public void setGoogleAnalyticsTrackingId(String googleAnalyticsTrackingId) {
        this.googleAnalyticsTrackingId = googleAnalyticsTrackingId;
    }

    public Image getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(Image companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyLogoPath() {
        return companyLogo == null ? "/images/logo.png" : "/images/image/"+companyLogo.getSha256();
    }

    public Image getTouchIcon() {
        return touchIcon;
    }

    public void setTouchIcon(Image touchIcon) {
        this.touchIcon = touchIcon;
    }
    
    public String getTouchIconPath() {
        return touchIcon == null ? "/images/touch-icon-192x192.png" : "/images/image/"+touchIcon.getSha256();
    }

    public String getFooterPrefix() {
        return footerPrefix;
    }

    public void setFooterPrefix(String footerPrefix) {
        this.footerPrefix = footerPrefix;
    }

    public String getDefaultEmail() {
        return defaultEmail;
    }

    public void setDefaultEmail(String defaultEmail) {
        this.defaultEmail = defaultEmail;
    }
}
