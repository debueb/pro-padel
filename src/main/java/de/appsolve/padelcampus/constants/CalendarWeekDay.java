/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dominik
 */
public enum CalendarWeekDay {

    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;

    public static Set valuesAsSet() {
        return new HashSet<>(Arrays.asList(CalendarWeekDay.values()));
    }
}
