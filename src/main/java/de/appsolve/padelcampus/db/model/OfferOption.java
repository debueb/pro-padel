/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.OfferOptionType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class OfferOption extends SortableEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.name}")
    private String name;
    
    @Column
    private String description;
    
    @Enumerated(EnumType.STRING)
    private OfferOptionType offerOptionType;
    
    @Column
    private String cameraUrl;
    
    @Column
    private Integer cameraPort;
    
    @Column
    private String cameraUser;
    
    @Column(length = 8000)
    private String cameraKey;
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description == null ? getName() : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public OfferOptionType getOfferOptionType() {
        return offerOptionType;
    }

    public void setOfferOptionType(OfferOptionType offerOptionType) {
        this.offerOptionType = offerOptionType;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    public Integer getCameraPort() {
        return cameraPort == null ? 22 : cameraPort;
    }

    public void setCameraPort(Integer cameraPort) {
        this.cameraPort = cameraPort;
    }

    public String getCameraUser() {
        return cameraUser;
    }

    public void setCameraUser(String cameraUser) {
        this.cameraUser = cameraUser;
    }

    public String getCameraKey() {
        return cameraKey;
    }

    public void setCameraKey(String cameraKey) {
        this.cameraKey = cameraKey;
    }

    @Override
    public String toString() {
        return getName();
    }
}