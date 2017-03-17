/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class Community extends ComparableEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.communityName}")
    private String name;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(BaseEntityI o) {
        if (!(o instanceof Community)){
            return super.compareTo(o);
        }
        Community other = (Community) o;
        return getName().compareTo(other.getName());
    }
}
