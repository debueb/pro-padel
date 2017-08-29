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

    /*
    saveOrUpdate() does the following:

    if the object is already persistent in this session, do nothing
    if another object associated with the session has the same identifier, throw an exception
    if the object has no identifier property, save() it
    if the object's identifier has the value assigned to a newly instantiated object, save() it
    if the object is versioned by a or , and the version property value is the same value assigned to a newly instantiated object, save() it
    otherwise update() the object

    and merge() is very different:

    if there is a persistent instance with the same identifier currently associated with the session, copy the state of the given object onto the persistent instance
    if there is no persistent instance currently associated with the session, try to load it from the database, or create a new persistent instance
    the persistent instance is returned
    the given instance does not become associated with the session, it remains detached


    problem: if we use merge() only and the object does yet not exist, hibernate tries to create a new peristent object which may fail due to validation errors
     */
    @Override
    public T saveOrUpdate(T entity) {
        setCustomer(entity);
        Session session = entityManager.unwrap(Session.class);
        if (entity.getId() == null) {
            Long id = (Long) session.save(entity);
            entity.setId(id);
        } else {
            entity = (T) session.merge(entity);
        }
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
