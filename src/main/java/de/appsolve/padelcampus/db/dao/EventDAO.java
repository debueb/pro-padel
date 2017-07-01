package de.appsolve.padelcampus.db.dao;
;
import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Event;
import java.util.List;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class EventDAO extends GenericDAO<Event> implements EventDAOI{

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
    public Event findByIdFetchWithGames(Long id) {
        return super.findByIdFetchEagerly(id, "games");
    }
    
    @Override
    public Event findByIdFetchWithParticipants(Long id) {
        return super.findByIdFetchEagerly(id, "participants");
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
    public Event findByIdFetchWithParticipantsAndGamesAndGameParticipantsAndGamePlayers(Long id) {
        return super.findByIdFetchEagerly(id, "participants", "games", "games.participants", "games.participants.players");
    }
    
    @Override
    public List<Event> findAllFetchWithParticipants() {
        return super.findAllFetchEagerly("participants");
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
    public List<Event> findAllFetchWithParticipantsAndPlayers() {
        return super.findAllFetchEagerly("participants", "participants.players");
    }

    @Override
    public List<Event> findAllActiveFetchWithParticipantsAndPlayers() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("active", true);
        return super.findAllFetchEagerlyWithAttributes(attributes, "participants", "participants.players");
    }
    
    @Override
    protected Set<String> getIndexedProperties(){
       return new HashSet<>(Arrays.asList("name")); 
    }
}