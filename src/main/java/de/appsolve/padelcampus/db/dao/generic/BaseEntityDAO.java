/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.dao.generic;

import de.appsolve.padelcampus.db.model.BaseEntityI;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.utils.CustomerUtil;
import de.appsolve.padelcampus.utils.GenericsUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * @param <T>
 * @author dominik
 */
@Repository
@Transactional
public abstract class BaseEntityDAO<T extends BaseEntityI> extends GenericsUtils<T> implements BaseEntityDAOI<T> {

    private static final Logger LOG = Logger.getLogger(BaseEntityDAO.class);

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
        Criteria criteria = getCriteria();
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List<T> list = (List<T>) criteria.list();
        sort(list);
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<T> findAllFetchEagerly(Pageable pageable, String... associations) {
        return findAllFetchEagerly(pageable, null, associations);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<T> findAllFetchEagerly(Pageable pageable, Set<Criterion> criterions, String... associations) {
        //http://stackoverflow.com/questions/2183617/criteria-api-returns-a-too-small-resultset

        //get the ids of every object that matches the pageable conditions
        //we cannot get the objects directly because we use FetchMode.JOIN which returns the scalar product of all rows in all affected tables
        //and CriteriaSpecification.DISTINCT_ROOT_ENTITY does not work on SQL Level but on in Java after the result is returned from SQL
        Criteria criteria = getPageableCriteria(pageable);
        if (criterions != null) {
            for (Criterion c : criterions) {
                criteria.add(c);
            }
        }
        criteria.setProjection(Projections.distinct(Projections.property("id")));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List<Long> list = criteria.list();

        //once we have the required ids we query for the complete objects
        Criteria objectCriteria = getCriteria();
        for (String association : associations) {
            objectCriteria.setFetchMode(association, FetchMode.JOIN);
        }
        if (!list.isEmpty()) {
            objectCriteria.add(Restrictions.in("id", list));
        }
        objectCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        addOrderBy(objectCriteria, pageable);
        List<T> objects = objectCriteria.list();
        sort(objects);

        //we also need the total number of rows
        Criteria rowCountCritieria = getCriteria();
        if (criterions != null) {
            for (Criterion c : criterions) {
                rowCountCritieria.add(c);
            }
        }
        rowCountCritieria.setProjection(Projections.rowCount());
        Long resultCount = (Long) rowCountCritieria.uniqueResult();
        if (resultCount == null) {
            resultCount = objects.size() + 0L;
        }
        Collections.sort(objects);
        PageImpl<T> page = new PageImpl<>(new ArrayList<>(objects), pageable, resultCount);
        return page;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<T> findAll(Pageable pageable) {
        return findAllFetchEagerly(pageable, new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(List<Long> ids) {
        Criterion[] criterion = new Criterion[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            criterion[i] = Restrictions.eq("id", ids.get(i));
        }
        Disjunction or = Restrictions.or(criterion);
        Criteria criteria = getCriteria();
        criteria.add(or);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List<T> list = (List<T>) criteria.list();
        sort(list);
        return list;
    }

    @Override
    public Page<T> findAllByFuzzySearch(String search, String... associations) {
        return findAllByFuzzySearch(search, null, associations);
    }

    @Override
    public Page<T> findAllByFuzzySearch(String search, Set<Criterion> criterions, String... associations) {
        Criteria criteria = getCriteria();
        for (String association : associations) {
            criteria.setFetchMode(association, FetchMode.JOIN);
        }
        List<Criterion> predicates = new ArrayList<>();
        for (String indexedPropery : getIndexedProperties()) {
            if (!StringUtils.isEmpty(search)) {
                String[] searchTerms = search.split(" ");
                for (String searchTerm : searchTerms) {
                    predicates.add(Restrictions.ilike(indexedPropery, searchTerm, MatchMode.ANYWHERE));
                }
            }
        }
        if (!predicates.isEmpty()) {
            criteria.add(Restrictions.or(predicates.toArray(new Criterion[predicates.size()])));
        }
        if (criterions != null) {
            for (Criterion c : criterions) {
                criteria.add(c);
            }
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<T> objects = criteria.list();
        sort(objects);
        PageImpl<T> page = new PageImpl<>(objects);
        return page;
    }

    @Override
    public Page<T> findAllByFuzzySearch(String search) {
        return findAllByFuzzySearch(search, new String[0]);
    }

    @Override
    public T findFirst() {
        List<T> allConfigs = findAll();
        if (allConfigs.isEmpty()) {
            return null;
        }
        return allConfigs.get(0);
    }

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


    problem: if we use merge() only and the object does yet not exist, hibernate clones the object before persisting it (failing to copy for instance @ElementCollection values) which leads to validation errors
     */
    @Override
    @SuppressWarnings("unchecked")
    public T saveOrUpdate(T entity) {
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
    public void saveOrUpdate(Collection<T> entities) {
        entities.forEach(entity -> {
            saveOrUpdate(entity);
        });
    }

    @Override
    public T saveOrUpdateWithMerge(T entity) {
        entity = entityManager.merge(entity);
        return saveOrUpdate(entity);
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
    public void delete(Collection<T> entities) {
        for (T entity : entities) {
            deleteById(entity.getId());
        }
    }

    @Override
    public List<T> findByAttributes(Map<String, Object> attributeMap) {
        Criteria criteria = getCriteria();
        for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
            criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<T> list = criteria.list();
        sort(list);
        return list;
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
            LOG.warn("Query for [type=" + getGenericSuperClassName(GenericDAO.class) + ", key=" + key + ", value=" + value + "] returned " + objects.size() + " results, expected 1.");
        }
        return objects.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findByUUIDFetchEagerly(final String uuid, String... associations) {
        Criteria criteria = getCriteria();
        for (String association : associations) {
            criteria.setFetchMode(association, FetchMode.JOIN);
        }
        criteria.add(Property.forName("UUID").eq(uuid));
        return (T) criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findByIdFetchEagerly(final long id, String... associations) {
        Criteria criteria = getCriteria();
        for (String association : associations) {
            criteria.setFetchMode(association, FetchMode.JOIN);
        }
        criteria.add(Property.forName("id").eq(id));
        return (T) criteria.uniqueResult();
    }

    @Override
    public List<T> findAllFetchEagerly(String... associations) {
        Criteria crit = getCriteria();
        for (String association : associations) {
            crit.setFetchMode(association, FetchMode.JOIN);
        }
        //we only want unique results
        //see http://stackoverflow.com/questions/18753245/one-to-many-relationship-gets-duplicate-objects-whithout-using-distinct-why
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) crit.list();
        sort(list);
        return list;
    }

    @Override
    public List<T> findAllFetchEagerlyWithAttributes(Map<String, Object> attributeMap, String... associations) {
        Criteria crit = getCriteria();
        for (String association : associations) {
            crit.setFetchMode(association, FetchMode.JOIN);
        }
        for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
            crit.add(Restrictions.eq(entry.getKey(), entry.getValue()));
        }
        //we only want unique results
        //see http://stackoverflow.com/questions/18753245/one-to-many-relationship-gets-duplicate-objects-whithout-using-distinct-why
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) crit.list();
        sort(list);
        return list;
    }

    protected Criteria getCriteria() {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        return criteria;
    }

    protected Customer getCustomer() {
        return CustomerUtil.getCustomer();
    }

    private Criteria getPageableCriteria(Pageable pageable) {
        Criteria criteria = getCriteria();
        addOrderBy(criteria, pageable);
        criteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        criteria.setMaxResults(pageable.getPageSize());
        return criteria;
    }

    private void addOrderBy(Criteria criteria, Pageable pageable) {
        Sort sort = pageable.getSort();
        if (sort != null) {
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                Order hibernateOrder;
                switch (order.getDirection()) {
                    case ASC:
                        hibernateOrder = Order.asc(order.getProperty());
                        break;
                    default:
                        hibernateOrder = Order.desc(order.getProperty());
                        break;
                }
                criteria.addOrder(hibernateOrder);
            }
        }
    }

    protected Set<String> getIndexedProperties() {
        return new HashSet<>();
    }


    protected void sort(List<T> list) {
        //allow subclasses to sort
    }
}
