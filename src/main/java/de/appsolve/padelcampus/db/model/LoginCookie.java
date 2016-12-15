/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.Entity;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 *
 * @author dominik
 */
@Entity
public class LoginCookie extends CustomerEntity{
    
    private static final long serialVersionUID = 1L;
    
    private String UUID;
    
    private String playerUUID;
    
    private String loginCookieHash;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate validUntil;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getLoginCookieHash() {
        return loginCookieHash;
    }

    public void setLoginCookieHash(String loginCookieHash) {
        this.loginCookieHash = loginCookieHash;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }
}