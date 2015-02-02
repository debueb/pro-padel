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
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class Offer extends BaseEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.name}")
    private String name;
    
    @Column
    @NotNull(message = "{NotEmpty.maxConcurrentBookings}")
    private Long maxConcurrentBookings;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMaxConcurrentBookings() {
        return maxConcurrentBookings;
    }

    public void setMaxConcurrentBookings(Long maxConcurrentBookings) {
        this.maxConcurrentBookings = maxConcurrentBookings;
    }

    @Override
    public String getDisplayName() {
        return name;
    }
    
    @Override
    public String toString(){
        return getDisplayName();
    }
}