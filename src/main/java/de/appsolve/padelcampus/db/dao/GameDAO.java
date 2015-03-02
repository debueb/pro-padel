package de.appsolve.padelcampus.db.dao;
;
import de.appsolve.padelcampus.db.model.Game;
import java.util.List;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class GameDAO extends GenericDAO<Game> implements GameDAOI{

   @Override
    public List<Game> findByEvent(Event event) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("event", event);
        return findByAttributes(attributes);
    }

    @Override
    public List<Game> findByParticipant(Participant participant) {
        List<Game> games = findAll();
        return filterByParticipant(games, participant);
    }

    @Override
    public List<Game> findByParticipantAndEvent(Participant participant, Event event) {
        List<Game> games = findByEvent(event);
        return filterByParticipant(games, participant);
    }

    private List<Game> filterByParticipant(List<Game> games, Participant participant) {
        Iterator<Game> iterator = games.iterator();
        while (iterator.hasNext()){
            Game game = iterator.next();
            if (!game.getParticipants().contains(participant)){
                iterator.remove();
            }
        }
        return games;
    }

    @Override
    public Game findByIdFetchWithTeams(Long id) {
        return super.findByIdFetchEagerly(id, "participants.players");
    }

    @Override
    public List<Game> findByParticipantAndEventWithScoreOnly(Participant participant, Event event) {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        criteria.add(Restrictions.isNotNull("scoreReporter"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return filterByParticipant((List<Game>) criteria.list(), participant);
    }
}