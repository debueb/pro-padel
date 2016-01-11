package de.appsolve.padelcampus.db.dao.generic;

import de.appsolve.padelcampus.db.model.CustomerEntity;
import java.util.List;
import java.util.Map;

public interface GenericDAOI<T extends CustomerEntity> extends BaseEntityDAOI<T>{

   T findByIdFetchEagerly(final long id, String... associations);
   
   public List<T> findAllFetchEagerlyWithAttributes(Map<String,Object> attributeMap, String... associations);
   
   public List<T> findAllFetchEagerly(String... associations);
}
