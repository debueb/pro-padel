/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.comparators;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author dominik
 */
public class MapValueComparator implements Comparator{
    
    Map map;
 
    public MapValueComparator(Map map) {
            this.map = map;
    }

    @Override
    public int compare(Object keyA, Object keyB) {
            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);
            return valueB.compareTo(valueA);
    }
    
    public static Map sortByValue(Map unsortedMap) {
        Map sortedMap = new TreeMap(new MapValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }
}
