package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Voucher;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class VoucherDAO extends GenericDAO<Voucher> implements VoucherDAOI{

    @Override
    public Voucher findByUUID(String UUID) {
        return findByAttribute("UUID", UUID);
    }

    @Override
    public List<Voucher> findExpiredBefore(LocalDate date) {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        criteria.add(Restrictions.lt("validUntil", date));
        criteria.add(Restrictions.eq("used", false));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Voucher>) criteria.list();
    }

}
