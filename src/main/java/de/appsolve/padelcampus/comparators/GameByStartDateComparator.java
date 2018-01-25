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
public class GameByStartDateComparator implements Comparator<Game>, Serializable {

    private static final long serialVersionUID = 1L;

    private Integer reverse = 1;

    public GameByStartDateComparator() {
    }

    public GameByStartDateComparator(Boolean reverse) {
        if (reverse) {
            this.reverse = -1;
        }
    }

    @Override
    public int compare(Game o1, Game o2) {
        int result = 0;
        if (o1.getStartDate() != null && o2.getStartDate() != null) {
            result = o1.getStartDate().compareTo(o2.getStartDate());
            if (result == 0) {
                if (o1.getStartTime() != null && o2.getStartTime() != null) {
                    result = o1.getStartTime().compareTo(o2.getStartTime());
                }
            }
        }
        if (result == 0) {
            if (o1.getEvent() != null && o2.getEvent() != null) {
                if (o1.getEvent().getStartDate() != null && o2.getEvent().getStartDate() != null) {
                    result = o1.getEvent().getStartDate().compareTo(o2.getEvent().getStartDate());
                    if (result == 0) {
                        result = o1.getId().compareTo(o2.getId());
                    }
                    return result * reverse;
                }
            }
            result = o1.getId().compareTo(o2.getId());
        }
        return result * reverse;
    }
}
