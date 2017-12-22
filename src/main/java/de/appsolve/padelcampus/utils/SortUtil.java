/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.comparators.MapValueComparator;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author dominik
 */
public class SortUtil {

    public static <K extends Comparable, V extends Comparable> SortedMap<K, V> sortMap(Map<K, V> map) {
        return sortMap(map, Boolean.FALSE);
    }

    public static <K extends Comparable, V extends Comparable> SortedMap<K, V> sortMap(Map<K, V> map, Boolean reverse) {
        @SuppressWarnings("unchecked")
        SortedMap<K, V> sortedMap = new TreeMap<>(new MapValueComparator<>(map, reverse));
        sortedMap.putAll(map);
        return sortedMap;
    }
}
