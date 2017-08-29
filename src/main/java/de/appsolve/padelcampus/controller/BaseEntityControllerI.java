/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.BaseEntityI;

/**
 * @param <T>
 * @author dominik
 */
public interface BaseEntityControllerI<T extends BaseEntityI> {

    public BaseEntityDAOI<T> getDAO();

    public String getModuleName();

}
