/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.dao.generic;

import de.appsolve.padelcampus.db.model.BaseEntityI;
import org.hibernate.criterion.Criterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <T>
 * @author dominik
 */
public interface BaseEntityDAOI<T extends BaseEntityI> {

    T findById(Long id);

    List<T> findAll();

    public Page<T> findAllFetchEagerly(Pageable pageable, String... associations);

    public Page<T> findAllFetchEagerly(Pageable pageable, Set<Criterion> criterions, String... associations);

    public Page<T> findAll(Pageable pageable);

    List<T> findAll(List<Long> ids);

    public Page<T> findAllByFuzzySearch(String search, String... associations);

    public Page<T> findAllByFuzzySearch(String search, Set<Criterion> criterions, String... associations);

    public Page<T> findAllByFuzzySearch(String search);

    T findFirst();

    T saveOrUpdate(T entity);

    void saveOrUpdate(Collection<T> entities);

    T saveOrUpdateWithMerge(T entity);

    void delete(T entity);

    void deleteById(Long id);

    void delete(Collection<T> entities);

    List<T> findByAttributes(Map<String, Object> attributeMap);

    T findByAttribute(String key, Object value);

    T findByIdFetchEagerly(final long id, String... associations);

    public T findByUUIDFetchEagerly(final String uuid, String... associations);

    public List<T> findAllFetchEagerlyWithAttributes(Map<String, Object> attributeMap, String... associations);

    public List<T> findAllFetchEagerly(String... associations);
}
