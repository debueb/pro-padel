/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.Gender;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dominik
 */
@Entity
@DiscriminatorValue("Team")
public class Team extends Participant {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotEmpty(message = "{NotEmpty.teamPlayers}")
    @OrderBy("firstName, lastName")
    private Set<Player> players;

    @ManyToOne
    private Community community;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Player> getPlayers() {
        return players == null ? new HashSet<>() : players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return getName();
    }
}
