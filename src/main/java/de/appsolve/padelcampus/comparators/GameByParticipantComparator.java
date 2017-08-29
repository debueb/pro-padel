/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.comparators;

import de.appsolve.padelcampus.db.model.Game;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author dominik
 */
public class GameByParticipantComparator implements Comparator<Game>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Game o1, Game o2) {
        return o1.toString().compareToIgnoreCase(o2.toString());
    }
}
