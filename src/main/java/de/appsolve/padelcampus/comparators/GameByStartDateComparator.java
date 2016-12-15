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
public class GameByStartDateComparator implements Comparator<Game>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public int compare(Game o1, Game o2) {
        if (o1.getStartDate()!=null && o2.getStartDate()!=null){
            return o1.getStartDate().compareTo(o2.getStartDate());
        }
        return o1.getId().compareTo(o2.getId());
    }
}
