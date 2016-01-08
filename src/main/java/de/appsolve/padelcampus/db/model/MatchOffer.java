/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.SkillLevel;
import de.appsolve.padelcampus.utils.FormatUtils;
import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;
import java.util.Collections;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author dominik
 */
@Entity
public class MatchOffer extends CustomerEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Player owner;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name = "MatchOffer_players", joinColumns = @JoinColumn(name = "matchoffer_id"), inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<Player> players;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name = "MatchOffer_waitingList", joinColumns = @JoinColumn(name = "matchoffer_id"), inverseJoinColumns = @JoinColumn(name = "waitingList_id"))
    private Set<Player> waitingList;
    
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate startDate;
    
    @Column
    private Integer startTimeHour;
    
    @Column
    private Integer startTimeMinute;
    
    @Column
    private Long duration;
    
    @Column
    private Integer minPlayersCount;
    
    @Column
    private Integer maxPlayersCount;

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
     
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "{NotEmpty.skillLevels}")
    private Set<SkillLevel> skillLevels;

    public Set<Player> getPlayers() {
        return players == null ? Collections.EMPTY_SET : players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Player> getWaitingList() {
        return waitingList == null ? Collections.EMPTY_SET : waitingList;
    }

    public void setWaitingList(Set<Player> waitingList) {
        this.waitingList = waitingList;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(Integer startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public Integer getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(Integer startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }
    
    public LocalTime getStartTime(){
        return TIME_HUMAN_READABLE.parseLocalTime(getStartTimeHour()+":"+getStartTimeMinute());
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
    
    public LocalTime getEndTime(){
        if (duration!=null){
            return getStartTime().plusMinutes(getDuration().intValue());
        }
        return null;
    }
    public Set<SkillLevel> getSkillLevels() {
        return skillLevels == null ? Collections.EMPTY_SET : skillLevels;
    }

    public void setSkillLevels(Set<SkillLevel> skillLevels) {
        this.skillLevels = skillLevels;
    }
    
    public Integer getMinPlayersCount(){
        return minPlayersCount;
    }

    public void setMinPlayersCount(Integer minPlayersCount){
        this.minPlayersCount = minPlayersCount;
    }
    
    public Integer getMaxPlayersCount() {
        return maxPlayersCount;
    }

    public void setMaxPlayersCount(Integer maxPlayersCount) {
        this.maxPlayersCount = maxPlayersCount;
    }
    
    @Override
    public String toString(){
        return getStartDate().toString(FormatUtils.DATE_WITH_DAY) + " " + getStartTime().toString(TIME_HUMAN_READABLE) + " - "+getEndTime().toString(TIME_HUMAN_READABLE);
    }
}
