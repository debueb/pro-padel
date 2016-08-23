/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class Offer extends SortableEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.name}")
    private String name;
    
    @Column(length = 3)
    @Pattern(regexp = "^.{1,3}", message = "{RegExp.ShortName}") 
    private String shortName;
    
    @Column
    @NotNull(message = "{NotEmpty.maxConcurrentBookings}")
    private Long maxConcurrentBookings;
    
    @Column
    private String hexColor;
    
    @Column
    private Boolean showInCalendar;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Long getMaxConcurrentBookings() {
        return maxConcurrentBookings == null ? 1L : maxConcurrentBookings;
    }

    public void setMaxConcurrentBookings(Long maxConcurrentBookings) {
        this.maxConcurrentBookings = maxConcurrentBookings;
    }

    public String getHexColor() {
        return hexColor == null ? "#69ff35" : hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public Boolean getShowInCalendar() {
        return showInCalendar == null ? Boolean.TRUE : showInCalendar;
    }

    public void setShowInCalendar(Boolean showInCalendar) {
        this.showInCalendar = showInCalendar;
    }

    @Override
    public String toString() {
        return getName();
    }
}