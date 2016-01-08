package de.appsolve.padelcampus.db.dao;

import java.util.List;
import java.util.Map;

public interface GenericDAOI<T> {

   T findById(Long id);

   T findByIdFetchEagerly(final long id, String... associations);
   
   List<T> findAllforAllCustomers();
   
   List<T> findAll();
   
   List<T> findAll(List<Long> ids);
   
   List<T> findAllFetchEagerly(String... associations);
   
   public List<T> findAllFetchEagerlyWithAttributes(Map<String,Object> attributeMap, String... associations);
   
   T findFirst();

   T saveOrUpdate(T entity);

   void delete(T entity);

   void deleteById(Long id);

   void delete(List<T> entities);
   
   List<T> findByAttributesForAllCustomers(Map<String, Object> attributeMap);
   
   List<T> findByAttributes(Map<String, Object> attributeMap);
   
   T findByAttribute(String key, Object value);
}
