package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.Voucher;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dominik
 */
@Component
public class VoucherBaseDAO extends BaseEntityDAO<Voucher> implements VoucherBaseDAOI {

    @Override
    @SuppressWarnings("unchecked")
    public List<Voucher> findExpiredBefore(LocalDate date) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.lt("validUntil", date));
        criteria.add(Restrictions.eq("used", false));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Voucher>) criteria.list();
    }

}
