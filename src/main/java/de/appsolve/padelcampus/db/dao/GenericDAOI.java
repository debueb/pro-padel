package de.appsolve.padelcampus.db.dao;

import java.util.List;
import java.util.Map;

public interface GenericDAOI<T> {

   T findById(Long id);

   T findByIdFetchEagerly(final long id, String... associations);
   
   List<T> findAll();
   
   List<T> findAllFetchEagerly(String... associations);
   
   T findFirst();

   T saveOrUpdate(T entity);

   void delete(T entity);

   void deleteById(Long id);

   void delete(List<T> entities);
   
   List<T> findByAttributes(Map<String, Object> attributeMap);
   
   T findByAttribute(String key, Object value);
}
