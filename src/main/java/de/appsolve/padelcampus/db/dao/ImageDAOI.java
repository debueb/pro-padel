/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAOI;
import de.appsolve.padelcampus.db.model.Image;

/**
 *
 * @author dominik
 */
public interface ImageDAOI extends GenericDAOI<Image>{
    
    Image findBySha256(String sha256);
}
