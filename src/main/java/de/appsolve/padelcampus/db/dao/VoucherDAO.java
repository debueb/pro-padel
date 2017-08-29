package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Voucher;
import org.springframework.stereotype.Component;

/**
 * @author dominik
 */
@Component
public class VoucherDAO extends GenericDAO<Voucher> implements VoucherDAOI {

    @Override
    public Voucher findByUUID(String UUID) {
        return findByAttribute("UUID", UUID);
    }
}
