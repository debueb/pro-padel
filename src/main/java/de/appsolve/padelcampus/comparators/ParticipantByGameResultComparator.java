/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.comparators;

import de.appsolve.padelcampus.db.model.Participant;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author dominik
 */
public class ParticipantByGameResultComparator implements Comparator<Participant>, Serializable {

    private static final long serialVersionUID = 1L;
    private final Participant sortByParticipant;
    private final Boolean reverseGameResult;

    public ParticipantByGameResultComparator(final Participant sortByParticipant, final Boolean reverseGameResult) {
        this.sortByParticipant = sortByParticipant;
        this.reverseGameResult = reverseGameResult;
    }

    @Override
    public int compare(Participant o1, Participant o2) {
        if (o1.equals(o2)) {
            return 0;
        }
        if (o1.equals(sortByParticipant)) {
            return reverseGameResult ? 1 : -1;
        }
        return reverseGameResult ? -1 : 1;
    }
}
