/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.data.CustomerI;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
}
