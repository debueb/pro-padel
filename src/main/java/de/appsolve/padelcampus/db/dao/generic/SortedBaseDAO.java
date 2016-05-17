package de.appsolve.padelcampus.db.dao.generic;

import de.appsolve.padelcampus.db.model.ComparableEntity;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public abstract class SortedBaseDAO<T extends ComparableEntity> extends BaseEntityDAO<T> {

    private static final Logger log = Logger.getLogger(SortedBaseDAO.class);

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
