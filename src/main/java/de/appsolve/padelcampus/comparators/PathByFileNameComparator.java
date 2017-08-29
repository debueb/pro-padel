/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.comparators;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * @author dominik
 */
public class PathByFileNameComparator implements Comparator<Path>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Path o1, Path o2) {
        return o1.getFileName().compareTo(o2.getFileName());
    }
}
