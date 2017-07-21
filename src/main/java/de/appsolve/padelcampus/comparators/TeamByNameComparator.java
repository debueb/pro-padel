/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.comparators;

import de.appsolve.padelcampus.db.model.Team;
import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author dominik
 */
public class TeamByNameComparator implements Comparator<Team>, Serializable{
    
    @Override
    public int compare(Team o1, Team o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
