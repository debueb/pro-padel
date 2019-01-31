package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Team;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;

;

/**
 * @author dominik
 */
@Component
public class EventDAO extends GenericDAO<Event> implements EventDAOI {

    @Override
    public List<Event> findByParticipant(Participant participant) {
        Criteria crit = getCriteria();
        crit.createAlias("participants", "p", JoinType.LEFT_OUTER_JOIN);
        crit.add(Restrictions.eq("p.id", participant.getId()));
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Event> events = (List<Event>) crit.list();
        sort(events);
        return events;
    }

    @Override
    public List<Event> findAllUpcomingWithParticipant(Team team) {
        Criteria crit = getCriteria();
        crit.createAlias("participants", "p", JoinType.LEFT_OUTER_JOIN);
        crit.add(Restrictions.eq("p.id", team.getId()));
        crit.add(Restrictions.eq("active", true));
        crit.add(Restrictions.ge("startDate", new LocalDate()));
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Event> events = (List<Event>) crit.list();
        sort(events);
        return events;
    }

    @Override
    public List<Event> findAllActive() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("active", true);
        return findByAttributes(attributes);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> findAllActiveStartingWith(LocalDate date) {
        Criteria crit = getCriteria();
        crit.add(Restrictions.eq("active", true));
        crit.add(Restrictions.ge("startDate", date));
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.addOrder(Order.asc("startDate"));
        return (List<Event>) crit.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> findAllActiveStartingWithEndingBefore(LocalDate startDate, LocalDate endDate) {
        Criteria crit = getCriteria();
        crit.add(Restrictions.eq("active", true));
        crit.add(Restrictions.ge("startDate", startDate));
        crit.add(Restrictions.lt("endDate", endDate));
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.addOrder(Order.asc("startDate"));
        return (List<Event>) crit.list();
    }

    @Override
    public Event findByIdFetchWithGames(Long id) {
        return super.findByIdFetchEagerly(id, "games");
    }

    @Override
    public Event findByIdFetchWithParticipants(Long id) {
        return super.findByIdFetchEagerly(id, "participants");
    }

    @Override
    public Event findByIdFetchWithParticipantsAndCommunities(Long id) {
        return super.findByIdFetchEagerly(id, "participants", "communities");
    }

    @Override
    public Event findByIdFetchWithParticipantsAndGames(Long id) {
        return super.findByIdFetchEagerly(id, "participants", "games");
    }

    @Override
    public Event findByIdFetchWithParticipantsAndPlayers(Long id) {
        return super.findByIdFetchEagerly(id, "participants", "participants.players");
    }

    @Override
    public List<Event> findAllFetchWithParticipants() {
        return super.findAllFetchEagerly("participants");
    }

    @Override
    public List<Event> findAllFetchWithParticipantsAndCommunities() {
        return super.findAllFetchEagerly("participants", "communities");
    }

    @Override
    public Page<Event> findAllFetchWithParticipantsAndPlayers(Pageable pageable) {
        return super.findAllFetchEagerly(pageable, "participants", "participants.players");
    }

    @Override
    public Page<Event> findAllFetchWithParticipantsAndPlayers(Pageable pageable, Set<Criterion> criterions) {
        return super.findAllFetchEagerly(pageable, criterions, "participants", "participants.players");
    }

    @Override
    public List<Event> findAllActiveFetchWithParticipantsAndPlayers() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("active", true);
        return super.findAllFetchEagerlyWithAttributes(attributes, "participants", "participants.players");
    }

    @Override
    protected Set<String> getIndexedProperties() {
        return new HashSet<>(Arrays.asList("name"));
    }
}