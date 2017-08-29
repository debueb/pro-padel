/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.comparators;

import de.appsolve.padelcampus.db.model.CssAttribute;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author dominik
 */
public class CssAttributeComparator implements Comparator<CssAttribute>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(CssAttribute o1, CssAttribute o2) {
        if (o1.getName().equals("customCss")) {
            return o2.getName().endsWith("Color") ? 1 : -1;
        }
        Boolean endsWith1 = o1.getName().endsWith("Color");
        Boolean endsWith2 = o2.getName().endsWith("Color");
        return endsWith2.compareTo(endsWith1);
    }
}
