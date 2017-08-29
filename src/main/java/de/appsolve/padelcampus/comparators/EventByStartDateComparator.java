/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.comparators;

import de.appsolve.padelcampus.db.model.Event;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author dominik
 */
public class EventByStartDateComparator implements Comparator<Event>, Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean reverse = false;

    public EventByStartDateComparator() {
    }

    public EventByStartDateComparator(Boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public int compare(Event o1, Event o2) {
        int result = o1.getStartDate().compareTo(o2.getStartDate());
        if (result == 0) {
            result = o1.getName().compareToIgnoreCase(o2.getName());
        }
        return reverse ? result * -1 : result;
    }
}
