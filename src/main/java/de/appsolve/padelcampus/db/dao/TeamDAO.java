package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedBaseDAO;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class TeamDAO extends SortedBaseDAO<Team> implements TeamDAOI{

    @Override
    public List<Team> findByPlayer(Player player) {
        List<Team> teams = new ArrayList<>();
        for (Team team: findAll()){
            for (Player teamPlayer: team.getPlayers()){
                if (teamPlayer.equals(player)){
                    teams.add(team);
                    break;
                }
            }
        }
        return teams;
    }

    @Override
    public Team findByIdFetchWithPlayers(Long id) {
        return super.findByIdFetchEagerly(id, "players");
    }

    @Override
    public List<Team> findAllFetchWithPlayers() {
        return super.findAllFetchEagerly("players");
    }
    
    @Override
    public Team saveOrUpdate(Team team){
        team.setCustomer(getCustomer());
        return super.saveOrUpdate(team);
    }
}
