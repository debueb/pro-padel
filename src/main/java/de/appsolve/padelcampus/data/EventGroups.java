/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Participant;

import java.util.Map;
import java.util.Set;

/**
 * @author dominik
 */
public class EventGroups {

    private Map<Integer, Set<Participant>> groupParticipants;

    public Map<Integer, Set<Participant>> getGroupParticipants() {
        return groupParticipants;
    }

    public void setGroupParticipants(Map<Integer, Set<Participant>> groupParticipants) {
        this.groupParticipants = groupParticipants;
    }
}
