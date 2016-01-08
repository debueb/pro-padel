/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Customer;

/**
 *
 * @author dominik
 */
public interface CustomerDAOI extends GenericDAOI<Customer>{
    
    public Customer findByDomainName(String domainName);
}
