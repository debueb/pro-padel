package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.News;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class NewsDAO extends SortedGenericDAO<News> implements NewsDAOI{
}
