package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedBaseDAO;
import de.appsolve.padelcampus.db.model.Participant;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ParticipantDAO extends SortedBaseDAO<Participant> implements ParticipantDAOI{
    
    @Override
    public Participant findByUUID(String UUID){
        return super.findByAttribute("UUID", UUID);
    }
}
