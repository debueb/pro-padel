package de.appsolve.padelcampus.db.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public abstract class SortedGenericDAO<T extends Comparable> extends GenericDAO<T> {

    private static final Logger log = Logger.getLogger(SortedGenericDAO.class);

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        List<T> results = super.findAll();
        Collections.sort(results);
        return results;
    }

    
    @Override
    public List<T> findByAttributes(Map<String, Object> attributeMap) {
        List<T> results = super.findByAttributes(attributeMap);
        Collections.sort(results);
        return results;
    }
}
