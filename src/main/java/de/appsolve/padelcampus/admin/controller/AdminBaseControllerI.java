/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.db.dao.GenericDAOI;
import de.appsolve.padelcampus.db.model.BaseEntity;

/**
 *
 * @author dominik
 * @param <T>
 */
public interface AdminBaseControllerI<T extends BaseEntity> {
    
    public GenericDAOI<T> getDAO();
    public String getModuleName();
    
}
