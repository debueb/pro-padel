/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.tasks;

import de.appsolve.padelcampus.db.dao.VoucherBaseDAOI;
import de.appsolve.padelcampus.db.model.Voucher;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class VoucherCleanupTask {
    
    private static final Logger log = Logger.getLogger(VoucherCleanupTask.class);
    
    @Autowired
    VoucherBaseDAOI voucherBaseDAO;
    
    @Scheduled(cron = "0 0 2 * * *") //second minute hour day month year, * = any, */5 = every 5
    public void deleteOldVouchers(){
        LocalDate now = new LocalDate();
        LocalDate oneMonthAgo = now.minusMonths(1);
        List<Voucher> expiredVouchers = voucherBaseDAO.findExpiredBefore(oneMonthAgo);
        log.info("Deleting "+expiredVouchers.size()+" Vouchers expired before "+oneMonthAgo);
        for (Voucher voucher: expiredVouchers){
            voucherBaseDAO.deleteById(voucher.getId());
        }
    }
    
}
