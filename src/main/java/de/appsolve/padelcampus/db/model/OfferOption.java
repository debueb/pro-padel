/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.OfferOptionType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * @author dominik
 */
@Entity
public class OfferOption extends SortableEntity {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column
    @NotEmpty(message = "{NotEmpty.name}")
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private OfferOptionType offerOptionType;

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

    @Override
    public String toString() {
        return getName();
    }
}