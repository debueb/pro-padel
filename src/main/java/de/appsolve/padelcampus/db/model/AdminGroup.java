/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.Privilege;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class AdminGroup extends CustomerEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @NotEmpty(message = "{NotEmpty.groupName}")
    @Column
    private String name;
    
    @NotEmpty(message = "{NotEmpty.members}")
    @ManyToMany(fetch=FetchType.EAGER)
    @OrderBy("firstName, lastName")
    private Set<Player> players;
    
    @NotEmpty(message = "{NotEmpty.privileges}")
    @ElementCollection(fetch=FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Privilege> privileges;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Player> getPlayers() {
        return players == null ? new HashSet<Player>() : players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Privilege> getPrivileges() {
        return privileges == null ? Collections.<Privilege>emptySet() : privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return name;
    }
}
