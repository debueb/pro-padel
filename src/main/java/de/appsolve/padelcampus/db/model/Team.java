/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.utils.TeamUtil;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
@DiscriminatorValue("Team")
public class Team extends Participant{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.teamName}")
    private String name;
    
    @ManyToMany(fetch=FetchType.LAZY)
    @NotEmpty(message = "{NotEmpty.teamPlayers}")
    @OrderBy("firstName, lastName")
    private Set<Player> players;
    
    @ManyToOne
    private Community community;

    public String getName() {
        String teamName = TeamUtil.getTeamName(this);
        if (!StringUtils.isEmpty(teamName)){
            return teamName;
        }
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

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    @Override
    public String toString() {
        return name;
    }
}
