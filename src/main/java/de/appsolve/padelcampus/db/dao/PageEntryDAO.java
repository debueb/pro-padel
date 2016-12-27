package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
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
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("module", module));
        criteria.setFirstResult(pageable.getOffset()+1);
        criteria.setMaxResults(pageable.getPageSize());
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<PageEntry> list = criteria.list();
        sort(list);
        
        Criteria countCriteria = getCriteria();
        countCriteria.add(Restrictions.eq("module", module));
        countCriteria.setProjection(Projections.rowCount());
        Long rowCount = (Long) countCriteria.uniqueResult();
        Page<PageEntry> page = new PageImpl(list, pageable, rowCount);
        return page;
    }
}
