package de.appsolve.padelcampus.db.dao;
;
import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import java.util.List;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class EventDAO extends GenericDAO<Event> implements EventDAOI{

    @Override
    public List<Event> findAllWithParticipant(Participant participant) {
        List<Event> allEvents = findAllFetchWithParticipants();
        List<Event> matchingEvents = new ArrayList<>();
        Iterator<Event> iterator = allEvents.iterator();
        while (iterator.hasNext()){
            Event event = iterator.next();
            if (event.getParticipants().contains(participant)){
                matchingEvents.add(event);
            } else if (participant instanceof Player){
                Player player = (Player) participant;
                for (Participant eventParticipant: event.getParticipants()){
                    if (eventParticipant instanceof Team){
                        Team team = (Team) eventParticipant;
                        if (team.getPlayers().contains(player)){
                            matchingEvents.add(event);
                        }
                    }
                }
            }
        }
        return matchingEvents;
    }

    @Override
    public List<Event> findAllActive() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("active", true);
        return findByAttributes(attributes);
    }

    @Override
    public Event findByIdFetchWithParticipants(Long id) {
        return super.findByIdFetchEagerly(id, "participants");
    }
    
    @Override
    public Event findByIdFetchWithParticipantsAndPlayers(Long id) {
        return super.findByIdFetchEagerly(id, "participants", "participants.players");
    }
    
    @Override
    public Event findByIdFetchWithParticipantsAndGames(Long id) {
        return super.findByIdFetchEagerly(id, "participants", "games");
    }
    
    @Override
    public List<Event> findAllFetchWithParticipants() {
        return super.findAllFetchEagerly("participants");
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
}