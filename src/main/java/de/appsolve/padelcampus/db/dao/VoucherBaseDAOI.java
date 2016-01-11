/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Voucher;
import java.util.List;
import org.joda.time.LocalDate;

/**
 *
 * @author dominik
 */
public interface VoucherBaseDAOI extends BaseEntityDAOI<Voucher>{
    
    public List<Voucher> findExpiredBefore(LocalDate oneMonthAgo);
}
