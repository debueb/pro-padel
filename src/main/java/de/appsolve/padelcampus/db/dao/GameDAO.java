package de.appsolve.padelcampus.db.dao;
;
import org.joda.time.LocalDate;
import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Game;
import java.util.List;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Team;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
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
    public List<Game> findAllYoungerThanWithPlayers(LocalDate date) {
        
        //does not work on Openshift
        //https://bugzilla.redhat.com/show_bug.cgi?id=1329068
        
//        Criteria criteria = getCriteria();
//        criteria.setFetchMode("participants.players", FetchMode.JOIN);
//        criteria.createAlias("event", "e");
//        criteria.add(Restrictions.gt("e.endDate", date));
//        criteria.add(Restrictions.isNotEmpty("gameSets"));
//        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        return (List<Game>) criteria.list();

        Criteria criteria = getCriteria();
        //criteria.setFetchMode("participants.players", FetchMode.JOIN);
        criteria.createAlias("event", "e");
        criteria.add(Restrictions.gt("e.endDate", date));
        criteria.add(Restrictions.isNotEmpty("gameSets"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Game> games = (List<Game>) criteria.list();
        for (Game game: games){
            for (Participant participant: game.getParticipants()){
                if (participant instanceof Team){
                    Team t = (Team)participant;
                    Hibernate.initialize(t.getPlayers());
                }
            }
        }
        return games;
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
    public Game findByIdFetchWithNextGame(Long id) {
        return super.findByIdFetchEagerly(id, "nextGame");
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