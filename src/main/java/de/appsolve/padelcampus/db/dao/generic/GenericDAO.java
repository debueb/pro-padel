package de.appsolve.padelcampus.db.dao.generic;

import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.CustomerEntity;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public abstract class GenericDAO<T extends CustomerEntity> extends BaseEntityDAO<T> {

    private static final Logger LOG = Logger.getLogger(GenericDAO.class);

    @Override
    public T saveOrUpdate(T entity) {
        setCustomer(entity);
        Session session = entityManager.unwrap(Session.class);
        session.merge(entity);
        return entity;
    }

    @Override
    protected Criteria getCriteria() {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        Customer customer = getCustomer();
        if (customer != null) {
            criteria.add(Restrictions.eq("customer", customer));
        }
        return criteria;
    }

    protected void setCustomer(T entity) {
        if (entity.getCustomer() == null) {
            Customer customer = getCustomer();
            entity.setCustomer(customer);
        }
    }
}
