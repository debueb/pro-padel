/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.dao.generic;

import de.appsolve.padelcampus.db.model.BaseEntityI;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dominik
 * @param <T>
 */
public interface BaseEntityDAOI<T extends BaseEntityI> {
    
   T findById(Long id);

   List<T> findAll();
   
   List<T> findAll(List<Long> ids);
   
   T findFirst();

   T saveOrUpdate(T entity);

   void delete(T entity);

   void deleteById(Long id);

   void delete(List<T> entities);
   
   List<T> findByAttributes(Map<String, Object> attributeMap);
   
   T findByAttribute(String key, Object value);
}
