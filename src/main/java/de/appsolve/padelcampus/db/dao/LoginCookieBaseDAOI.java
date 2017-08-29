/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.LoginCookie;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * @author dominik
 */
public interface LoginCookieBaseDAOI extends BaseEntityDAOI<LoginCookie> {

    List<LoginCookie> findExpiredBefore(LocalDate date);
}
