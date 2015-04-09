/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Participant;

/**
 *
 * @author dominik
 */
public class ScoreEntry implements Comparable<ScoreEntry>{
    
    private Participant participant;
    
    private int totalPoints;
    
    private int matchesPlayed;
    
    private int matchesWon;
    
    private int setsPlayed;
    
    private int setsWon;
    
    private int gamesPlayed;
    
    private int gamesWon;

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getSetsPlayed() {
        return setsPlayed;
    }

    public void setSetsPlayed(int setsPlayed) {
        this.setsPlayed = setsPlayed;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    
    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(int matchesWon) {
        this.matchesWon = matchesWon;
    }

    public int getSetsWon() {
        return setsWon;
    }

    public void setSetsWon(int setsWon) {
        this.setsWon = setsWon;
    }
    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    @Override
    public int compareTo(ScoreEntry o) {
        int matchDiff = o.totalPoints-totalPoints;
        if (matchDiff!=0){
            return matchDiff;
        }
        
        int setsWonDiff = o.setsWon-setsWon;
        if (setsWonDiff!=0){
            return setsWonDiff;
        }
        
        int gamesWonDiff = o.gamesWon-gamesWon;
        if (gamesWonDiff!=0){
            return gamesWonDiff;
        }
        
        int gamesLost = gamesPlayed-gamesWon;
        int oGamesLost = o.gamesPlayed-o.gamesWon;
        
        int gamesLostDiff = gamesLost - oGamesLost;
        if (gamesLostDiff!=0){
            return gamesLostDiff;
        }
        
        if (participant!=null && o.getParticipant()!=null){
            return participant.toString().compareToIgnoreCase(o.getParticipant().toString());
        }
        return -1;
    }
}
