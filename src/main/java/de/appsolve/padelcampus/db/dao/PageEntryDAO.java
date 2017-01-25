package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class PageEntryDAO extends SortedGenericDAO<PageEntry> implements PageEntryDAOI{

    @Override
    public List<PageEntry> findByModule(Module module) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("module", module);
        return findByAttributes(attributes);
    }
    
    @Override
    public Page<PageEntry> findByModule(Module module, Pageable pageable) {
        Criteria countCriteria = getCriteria();
        countCriteria.add(Restrictions.eq("module", module));
        countCriteria.setProjection(Projections.rowCount());
        Long rowCount = (Long) countCriteria.uniqueResult();
        if (rowCount == 0L){
            return new PageImpl<>(new ArrayList<PageEntry>(), pageable, rowCount);
        }
        
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("module", module));
        criteria.setMaxResults(pageable.getPageSize());
        criteria.setFirstResult(pageable.getOffset());
        criteria.setProjection(Projections.distinct(Projections.property("id")));
        criteria.addOrder(Order.asc("position"));
        @SuppressWarnings("unchecked")
        List<Long> list = criteria.list();
        
        Criteria c = getCriteria();
        c.add(Restrictions.eq("module", module));
        c.add(Restrictions.in("id", list));
        c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<PageEntry> pageEntries = c.list();
        sort(pageEntries);
        
        
        Page<PageEntry> page = new PageImpl<>(pageEntries, pageable, rowCount);
        return page;
    }
}
