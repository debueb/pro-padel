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
 *
 * @author dominik
 */
public class EventByStartDateComparator implements Comparator<Event>, Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public int compare(Event o1, Event o2) {
        return o1.getStartDate().compareTo(o2.getStartDate());
    }
}
