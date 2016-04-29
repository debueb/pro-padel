/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.Gender;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

/**
 *
 * @author dominik
 */
@Entity
public class Event extends ComparableEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.eventName}")
    private String name;
    
    @Column
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column
    private Integer numberOfGroups;
    
    @Column
    private Boolean active;
    
    @ManyToMany(fetch=FetchType.LAZY)
    @NotEmpty(message = "{NotEmpty.participants}")
    private Set<Participant> participants;
    
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate startDate;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate endDate;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="event", cascade = CascadeType.REMOVE)
    private Set<Game> games;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getEventType() {
        return eventType == null ? EventType.SingleRoundRobin : eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Gender getGender() {
        return gender == null ? Gender.male : gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getNumberOfGroups() {
        return numberOfGroups == null ? 1 : numberOfGroups;
    }

    public void setNumberOfGroups(Integer numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Participant> getParticipants() {
        return participants == null ? new LinkedHashSet<Participant>() : participants;
    }
    
    public void setParticipants(Set<Participant> participant) {
        this.participants = participant;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate dateTime) {
        this.startDate = dateTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
    
    public Set<Team> getTeams(){
        Set<Team> teams = new TreeSet<>();
        for (Participant participant: participants){
            if (participant instanceof Team){
                teams.add((Team)participant);
            }
        }
        return teams;
    }
    
    public Set<Player> getPlayers(){
        Set<Player> players = new TreeSet<>();
        for (Participant participant: participants){
            if (participant instanceof Player){
                players.add((Player)participant);
            }
        }
        return players;
    }
    
    @Override
    public String toString() {
        return name;
    }
}