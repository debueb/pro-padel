package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.utils.GenericsUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public abstract class GenericDAO<T> extends GenericsUtils<T> implements GenericDAOI<T> {

    private static final Logger log = Logger.getLogger(GenericDAO.class);

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public T findById(Long id) {
        return entityManager.find(getGenericSuperClass(GenericDAO.class), id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<T>) criteria.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(List<Long> ids) {
        Session session = entityManager.unwrap(Session.class);
        Criterion[] criterion = new Criterion[ids.size()];
        for (int i=0; i<ids.size(); i++){
            criterion[i] = Restrictions.eq("id", ids.get(i));
        }
        Disjunction or = Restrictions.or(criterion);
        Criteria criteria = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        criteria.add(or);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<T>) criteria.list();
    }

    @Override
    public T findFirst() {
        List<T> allConfigs = findAll();
        if (allConfigs.isEmpty()) {
            return null;
        }
        return allConfigs.get(0);
    }

    @Override
    public T saveOrUpdate(T entity) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void delete(T entity) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        delete(findById(id));
    }

    @Override
    public void delete(List<T> entities) {
        Session session = entityManager.unwrap(Session.class);
        for (T entity : entities) {
            session.delete(entity);
        }
    }

    @Override
    public List<T> findByAttributes(Map<String, Object> attributeMap) {
       //hibernate specific way of getting results

        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
            criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<T>) criteria.list();

        //alternative way of getting results without using session == the JPA way
//       CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//       Class<T> clazz = getGenericSuperClass(GenericDAO.class);
//       CriteriaQuery<T> cq = cb.createQuery(clazz);
//       Root<T> c = cq.from(clazz);
//       Predicate[] predicates = new Predicate[attributeMap.size()];
//       int i = 0;
//       for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
//           predicates[i] = cb.equal(c.get(entry.getKey()), entry.getValue());
//           i++;
//       }
//       cq.where(predicates);
//       TypedQuery<T> q = entityManager.createQuery(cq);
//       List<T> results = q.getResultList();
//       return results;
    }

    @Override
    public T findByAttribute(String key, Object value) {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put(key, value);
        List<T> objects = findByAttributes(attributeMap);
        if (objects == null || objects.isEmpty()) {
            return null;
        }
        if (objects.size() != 1) {
            log.warn("Query for [type=" + getGenericSuperClassName(GenericDAO.class) + ", key=" + key + ", value=" + value + "] returned " + objects.size() + " results, expected 1.");
        }
        return objects.get(0);
    }
    
    @Override
    public T findByIdFetchEagerly(final long id, String... associations) {
        Session session = entityManager.unwrap(Session.class);
        final Criteria crit = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        for (String association: associations){
            crit.setFetchMode(association, FetchMode.JOIN);
        }
        crit.add(Property.forName("id").eq(id));
        return (T) crit.uniqueResult();
    }
    
    @Override
    public List<T> findAllFetchEagerly(String... associations){
        Session session = entityManager.unwrap(Session.class);
        final Criteria crit = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        for (String association: associations){
            crit.setFetchMode(association, FetchMode.JOIN);
        }
        //we only want unique results
        //see http://stackoverflow.com/questions/18753245/one-to-many-relationship-gets-duplicate-objects-whithout-using-distinct-why
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return crit.list();
    }
    
    @Override
    public List<T> findAllFetchEagerlyWithAttributes(Map<String,Object> attributeMap, String... associations){
        Session session = entityManager.unwrap(Session.class);
        final Criteria crit = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        for (String association: associations){
            crit.setFetchMode(association, FetchMode.JOIN);
        }
        //we only want unique results
        //see http://stackoverflow.com/questions/18753245/one-to-many-relationship-gets-duplicate-objects-whithout-using-distinct-why
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return crit.list();
    }
}
