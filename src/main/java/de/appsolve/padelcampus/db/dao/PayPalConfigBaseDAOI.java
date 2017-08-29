/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.PayPalConfig;

/**
 * @author dominik
 */
public interface PayPalConfigBaseDAOI extends BaseEntityDAOI<PayPalConfig> {

    PayPalConfig findByCustomer(Customer customer);

}
