package de.appsolve.padelcampus.db.dao.generic;

import de.appsolve.padelcampus.db.model.ComparableEntity;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public abstract class SortedGenericDAO<T extends ComparableEntity> extends GenericDAO<T> {

    private static final Logger log = Logger.getLogger(SortedGenericDAO.class);

    public T findByPosition(Long position){
        return super.findByAttribute("position", position);
    }
    
    @Override
    public void sort(List<T> list){
        Collections.sort(list);
    }
}
