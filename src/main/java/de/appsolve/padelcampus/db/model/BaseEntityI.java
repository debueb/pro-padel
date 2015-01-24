/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.model;

import java.io.Serializable;

/**
 *
 * @author dominik
 */
public interface BaseEntityI extends Serializable, Comparable<BaseEntityI>{
    
    public String getDisplayName();
}
