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
 *
 * @author dominik
 */
public class GameByEventComparator implements Comparator<Game>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public int compare(Game o1, Game o2) {
        int result = 0;
        if (o1.getEvent() != null && o2.getEvent() != null){
            result = o1.getEvent().getName().compareToIgnoreCase(o2.getEvent().getName());
        } 
        if (result == 0){
            result = o1.getId().compareTo(o2.getId());
        }
        return result;
    }
}
