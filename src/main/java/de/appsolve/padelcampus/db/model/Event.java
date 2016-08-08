/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.utils.MailUtils;
import java.util.HashSet;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
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
    private Integer numberOfWinnersPerGroup;
    
    @Column
    private Integer numberOfSets;
    
    @Column
    private Integer numberOfGamesPerSet;
    
    @Column
    private Integer numberOfGamesInFinalSet;
    
    @Column
    private Boolean active;
    
    @ManyToMany(fetch=FetchType.LAZY)
    private Set<Participant> participants;
    
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate startDate;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate endDate;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="event", cascade = CascadeType.REMOVE)
    @OrderBy(value = "id")
    private Set<Game> games;
    
    @Column
    private String location;
    
    @Column(length=8000)
    private String description;
    
    @OneToOne(fetch = FetchType.LAZY)
    private CalendarConfig calendarConfig;

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
        return numberOfGroups == null ? 2 : numberOfGroups;
    }

    public void setNumberOfGroups(Integer numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
    }

    public Integer getNumberOfWinnersPerGroup() {
        return numberOfWinnersPerGroup == null ? 2 : numberOfWinnersPerGroup;
    }

    public void setNumberOfWinnersPerGroup(Integer numberOfWinnersPerGroup) {
        this.numberOfWinnersPerGroup = numberOfWinnersPerGroup;
    }

    public Integer getNumberOfSets() {
        return numberOfSets == null ? 3 : numberOfSets;
    }

    public void setNumberOfSets(Integer numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    public Integer getNumberOfGamesPerSet() {
        return numberOfGamesPerSet == null ? 7 : numberOfGamesPerSet;
    }

    public void setNumberOfGamesPerSet(Integer numberOfGamesPerSet) {
        this.numberOfGamesPerSet = numberOfGamesPerSet;
    }

    public Integer getNumberOfGamesInFinalSet() {
        return numberOfGamesInFinalSet == null ? 1 : numberOfGamesInFinalSet;
    }

    public void setNumberOfGamesInFinalSet(Integer numberOfGamesInFinalSet) {
        this.numberOfGamesInFinalSet = numberOfGamesInFinalSet;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CalendarConfig getCalendarConfig() {
        return calendarConfig;
    }

    public void setCalendarConfig(CalendarConfig calendarConfig) {
        this.calendarConfig = calendarConfig;
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
    
    @Transient
    public String getMailTo(){
        Set<Player> players = new TreeSet<>(getPlayers());
        for (Team team: getTeams()){
            players.addAll(team.getPlayers());
        }
        return MailUtils.getMailTo(players);
    }
}