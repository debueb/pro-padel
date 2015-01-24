package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.AdminGroup;
import de.appsolve.padelcampus.db.model.Player;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class AdminGroupDAO extends GenericDAO<AdminGroup> implements AdminGroupDAOI{

    @Override
    public List<AdminGroup> findByPlayer(Player player) {
        List<AdminGroup> groups = findAll();
        Iterator<AdminGroup> iterator = groups.iterator();
        while (iterator.hasNext()){
            AdminGroup group = iterator.next();
            if (!group.getPlayers().contains(player)){
                iterator.remove();
            }
        }
        return groups;
    }
}
