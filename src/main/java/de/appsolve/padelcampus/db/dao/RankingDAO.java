package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.Ranking;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dominik
 */
@Component
public class RankingDAO extends BaseEntityDAO<Ranking> implements RankingDAOI {

    @Override
    @SuppressWarnings("unchecked")
    public List<Ranking> findByDate(LocalDate date) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("date", date);
        List<Ranking> rankings = super.findByAttributes(attributes);
        Collections.sort(rankings);
        return rankings;
    }
}
