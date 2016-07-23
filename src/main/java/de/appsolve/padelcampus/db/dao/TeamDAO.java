package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedBaseDAO;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class TeamDAO extends SortedBaseDAO<Team> implements TeamDAOI{

    @Override
    public Team findByUUID(String UUID){
        return super.findByAttribute("UUID", UUID);
    }
    
    @Override
    public Team findByPlayers(Set<Player> players){
        Map<Player, List<Team>> playerTeamMap = new HashMap<>();
        for (Player player: players){
            playerTeamMap.put(player, findByPlayer(player));
        }
        List<Team> allTeams = new ArrayList<>();
        for (Player player: players){
            allTeams.addAll(playerTeamMap.get(player));
        }
        Iterator<Team> teamsIterator = allTeams.iterator();
        while (teamsIterator.hasNext()){
            Team team = teamsIterator.next();
            for (Player player: players){
                List<Team> playerTeams = playerTeamMap.get(player);
                if (!playerTeams.contains(team)){
                    teamsIterator.remove();
                }
            }
        }
        if (!allTeams.isEmpty()){
            return allTeams.get(0);
        }
        return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Team> findByPlayer(Player player) {
        if (player == null){
            return Collections.<Team>emptyList();
        }
        Criteria criteria = getCriteria();
        criteria.createAlias("players", "p");
        criteria.add(Restrictions.eq("p.id", player.getId()));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<Team>) criteria.list();
    }

    @Override
    public Team findByIdFetchWithPlayers(Long id) {
        return super.findByIdFetchEagerly(id, "players");
    }
    
    @Override
    public Team findByUUIDFetchWithPlayers(String uuid) {
        return super.findByUUIDFetchEagerly(uuid, "players");
    }

    @Override
    public Page<Team> findAllFetchWithPlayers(Pageable pageable) {
        Page<Team> teams= super.findAllFetchEagerly(pageable, "players");
        return teams;
    }
    
    @Override
    public Team saveOrUpdate(Team team){
        team.setCustomer(getCustomer());
        if (StringUtils.isEmpty(team.getUUID())){
            team.setUUID(UUID.randomUUID().toString());
        }
        return super.saveOrUpdate(team);
    }
    
    @Override
    protected Set<String> getIndexedProperties(){
       return new HashSet<>(Arrays.asList("name")); 
    }
}
