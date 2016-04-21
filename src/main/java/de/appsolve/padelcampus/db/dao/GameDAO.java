package de.appsolve.padelcampus.db.dao;
;
import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Game;
import java.util.List;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
    public List<Game> findAllWithPlayers(){
        return super.findAllFetchEagerly("participants.players");
    }

    
    @Override
    public List<Game> findByEventWithPlayers(Event event) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("event", event);
        return findAllFetchEagerlyWithAttributes(attributes, "participants.players");
    }
    
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
    public Game findByIdFetchWithTeamsAndScoreReporter(Long id) {
        return super.findByIdFetchEagerly(id, "participants.players", "scoreReporter");
    }

    @Override
    public List<Game> findByParticipantAndEventWithScoreOnly(Participant participant, Event event) {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(GameSet.class, "gameset");
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.createAlias("gameset.game", "game");
        //criteria.add(Restrictions.eq("game", "gameset.Game"));
        criteria.add(Restrictions.eq("game.event", event));
        List<GameSet> gameSets = (List<GameSet>) criteria.list();
        Set<Game> games = new HashSet<>();
        for (GameSet gameSet: gameSets){
            games.add(gameSet.getGame());
        }
        return filterByParticipant(new ArrayList<>(games), participant);
    }
}