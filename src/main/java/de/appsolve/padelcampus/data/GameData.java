/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dominik
 */
public class GameData {
    
    private Long id;
    private List<Participant> participantList;

    public GameData(){
    }
    
    public GameData(Game game){
        this.id = game.getId();
        this.participantList = new ArrayList<>();
        for (Participant p: game.getParticipants()){
            if (p!=null){
                participantList.add(p);
            }
        }
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
    }
    
}
