/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import static de.appsolve.padelcampus.constants.Constants.UTF8;
import de.appsolve.padelcampus.utils.CryptUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author dominik
 */
@Entity
public class Game extends CustomerEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    private Event event;
    
    @ManyToMany(fetch=FetchType.EAGER)
    @OrderBy(value = "name")
    private Set<Participant> participants;
    
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<GameSet> gameSets;
    
    @OneToOne(fetch=FetchType.LAZY)
    private Player scoreReporter;
    
    @Column
    private String voucherUUID;
    
    @Column
    private Integer round;
    
    @Column
    private Integer groupNumber;
    
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate startDate;
    
    @Column
    private Integer startTimeHour;
    
    @Column
    private Integer startTimeMinute;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Game nextGame;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    
    public Set<Participant> getParticipants() {
        return participants == null ? new LinkedHashSet<Participant>() : participants;
    }

    public void setParticipants(Set<Participant> participant) {
        this.participants = participant;
    }

    public Set<GameSet> getGameSets() {
        return gameSets == null ? new LinkedHashSet<GameSet>() : gameSets;
    }

    public void setGameSets(Set<GameSet> gameSets) {
        this.gameSets = gameSets;
    }

    public Player getScoreReporter() {
        return scoreReporter;
    }

    public void setScoreReporter(Player scoreReporter) {
        this.scoreReporter = scoreReporter;
    }

    public String getVoucherUUID() {
        return voucherUUID;
    }

    public void setVoucherUUID(String voucherUUID) {
        this.voucherUUID = voucherUUID;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(Integer startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public Integer getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(Integer startTimeHour) {
        this.startTimeHour = startTimeHour;
    }
    
    public LocalTime getStartTime(){
        if (getStartTimeHour() != null && getStartTimeMinute() != null){
            return TIME_HUMAN_READABLE.parseLocalTime(getStartTimeHour()+":"+getStartTimeMinute());
        }
        return null;
    }

    public Game getNextGame() {
        return nextGame;
    }

    public void setNextGame(Game nextGame) {
        this.nextGame = nextGame;
    }
    
    public String getObfuscatedMailTo() throws UnsupportedEncodingException{
        StringBuilder builder = new StringBuilder(CryptUtil.rot47("?cc="));
        for (Participant participant: getParticipants()){
            if (participant instanceof Team){
                Team team = (Team) participant;
                for (Player player: team.getPlayers()){
                    builder.append(player.getObfuscatedEmail());
                    builder.append(CryptUtil.rot47(","));
                }
            } else if (participant instanceof Player){
                Player player = (Player) participant;
                builder.append(player.getObfuscatedEmail());
                builder.append(CryptUtil.rot47(","));
            }
        }
        if (builder.length()>4){
            int i = 0;
                builder.append(CryptUtil.rot47("&subject="));
            for (Participant participant: getParticipants()){
                builder.append(CryptUtil.rot47(URLEncoder.encode(participant.toString(), UTF8)));
                if (i<getParticipants().size()-1){
                    builder.append(CryptUtil.rot47(" vs. "));
                }
                i++;
            }
            return builder.toString();
        }
        return "";
    }
    
    public Set<Player> getPlayers(){
        Set<Player> players = new HashSet<>();
        for (Participant p: getParticipants()){
            if (p instanceof Player){
                players.add((Player)p);
            } else if (p instanceof Team){
                Team team = (Team) p;
                players.addAll(team.getPlayers());
            }
        }
        return players;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Participant participant: getParticipants()){
            sb.append(participant.toString());
            sb.append(" vs. ");
        }
        if (sb.length()>0){
            return sb.substring(0, sb.length()-" vs. ".length());
        }
        return sb.toString();
    }
}