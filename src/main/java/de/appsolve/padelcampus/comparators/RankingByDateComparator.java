/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.comparators;

import de.appsolve.padelcampus.db.model.Ranking;

import java.util.Comparator;

/**
 * @author dominik
 */
public class RankingByDateComparator implements Comparator<Ranking> {

    @Override
    public int compare(Ranking o1, Ranking o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
