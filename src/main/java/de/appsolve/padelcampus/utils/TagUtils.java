/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import java.util.Collection;

/**
 * @author dominik
 */
public class TagUtils {

    public static boolean contains(Collection<?> coll, Object o) {
        return coll.contains(o);
    }

    public static Integer min(Integer a, Integer b) {
        return Math.min(a, b);
    }
}
