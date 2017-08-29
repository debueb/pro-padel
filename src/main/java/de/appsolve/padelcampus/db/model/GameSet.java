/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.*;

/**
 * @author dominik
 */
@Entity
public class GameSet extends CustomerEntity {

    @Transient
    private static final long serialVersionUID = 1L;

    @OneToOne
    private Game game;

    @OneToOne
    private Participant participant;

    //event needs to be fetched lazily for scores to work reasonably fast
    @OneToOne(fetch = FetchType.LAZY)
    private Event event;

    @Column
    private Integer setNumber;

    @Column
    private Integer setGames;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getSetGames() {
        return setGames;
    }

    public void setSetGames(Integer setGames) {
        this.setGames = setGames;
    }

    @Override
    public int compareTo(BaseEntityI o) {
        if (o instanceof GameSet) {
            GameSet other = (GameSet) o;
            //sort by set first, then by participant
            int r1 = getSetNumber() - other.getSetNumber();
            if (r1 != 0) {
                return r1;
            }
            return getParticipant().compareTo(other.getParticipant());
        }
        return super.compareTo(o);
    }
}