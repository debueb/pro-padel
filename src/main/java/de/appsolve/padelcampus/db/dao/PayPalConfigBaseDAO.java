package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.PayPalConfig;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class PayPalConfigBaseDAO extends BaseEntityDAO<PayPalConfig> implements PayPalConfigBaseDAOI{

    @Override
    public PayPalConfig findByCustomer(Customer customer) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("customer", customer));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List<PayPalConfig> configs = (List<PayPalConfig>) criteria.list();
        if (configs.size()>0){
            return configs.get(0);
        }
        return null;
    }
}
