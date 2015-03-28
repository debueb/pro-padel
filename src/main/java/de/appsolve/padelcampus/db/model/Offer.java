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
    
    @Column
    private String hexColor;
    
    @Column
    private Long position;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getPosition() {
        return position == null ? 0 : position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(BaseEntityI o) {
        if (o instanceof Offer){
            Offer other = (Offer) o;
            return this.getPosition().compareTo(other.getPosition());
        }
        return super.compareTo(o);
    }
    
   
}