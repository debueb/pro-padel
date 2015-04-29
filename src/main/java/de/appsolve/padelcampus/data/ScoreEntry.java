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
        //points
        int diff = o.totalPoints-totalPoints;
        if (diff!=0){
            return diff;
        }
        
        //matches
        diff = getDiff(matchesPlayed, matchesWon, o.matchesPlayed, o.matchesWon);
        if (diff!=0){
            return diff;
        }
        
        //sets
        diff = getDiff(setsPlayed, setsWon, o.setsPlayed, o.setsWon);
        if (diff!=0){
            return diff;
        }
        
        //games
        diff = getDiff(gamesPlayed, gamesWon, o.gamesPlayed, o.gamesWon);
        if (diff!=0){
            return diff;
        }
        
        if (participant!=null && o.getParticipant()!=null){
            return participant.toString().compareToIgnoreCase(o.getParticipant().toString());
        }
        return -1;
    }

    private int getDiff(int played, int won, int oPlayed, int oWon) {
        int lost = played-won;
        int diff = won - lost;
        int oLost = oPlayed-oWon;
        int oDiff = oWon - oLost;
        return oDiff - diff;
    }
}
