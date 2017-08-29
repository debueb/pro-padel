package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedBaseDAO;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.utils.TeamUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author dominik
 */
@Component
public class TeamDAO extends SortedBaseDAO<Team> implements TeamDAOI {

    private static final String ALIAS_PREFIX = "alias_";

    @Override
    public Team findByUUID(String UUID) {
        return super.findByAttribute("UUID", UUID);
    }

    @Override
    public Team findByPlayers(Set<Player> players) {
        Map<Player, List<Team>> playerTeamMap = new HashMap<>();
        for (Player player : players) {
            playerTeamMap.put(player, findByPlayer(player));
        }
        List<Team> allTeams = new ArrayList<>();
        for (Player player : players) {
            allTeams.addAll(playerTeamMap.get(player));
        }
        Iterator<Team> teamsIterator = allTeams.iterator();
        while (teamsIterator.hasNext()) {
            Team team = teamsIterator.next();
            for (Player player : players) {
                List<Team> playerTeams = playerTeamMap.get(player);
                if (!playerTeams.contains(team)) {
                    teamsIterator.remove();
                }
            }
        }
        if (!allTeams.isEmpty()) {
            return allTeams.get(0);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Team> findByPlayer(Player player) {
        if (player == null) {
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
        Page<Team> teams = super.findAllFetchEagerly(pageable, "players");
        return teams;
    }

    @Override
    public Team saveOrUpdate(Team team) {
        team.setCustomer(getCustomer());
        if (StringUtils.isEmpty(team.getUUID())) {
            team.setUUID(UUID.randomUUID().toString());
        }
        return super.saveOrUpdate(team);
    }


    @Override
    public Page<Team> findAllByFuzzySearch(String search, String... associations) {
        Criteria criteria = getCriteria();
        for (String association : associations) {
            criteria.createAlias(association, ALIAS_PREFIX + association, JoinType.INNER_JOIN);
        }
        List<Criterion> predicates = new ArrayList<>();
        for (String indexedPropery : getIndexedProperties()) {
            predicates.add(Restrictions.ilike(indexedPropery, search, MatchMode.ANYWHERE));
        }
        if (!predicates.isEmpty()) {
            criteria.add(Restrictions.or(predicates.toArray(new Criterion[predicates.size()])));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Team> objects = criteria.list();
        sort(objects);
        PageImpl<Team> page = new PageImpl<>(objects);
        return page;
    }

    @Override
    public Page<Team> findAllByFuzzySearch(String search) {
        Page<Team> teams = findAllByFuzzySearch(search, new String[]{"players"});
        //by querying the size of the players we force jpa proxy to initialize the lazy relation
        for (Team team : teams) {
            team.getPlayers().size();
        }
        return teams;
    }

    @Override
    public Team findOrCreateTeam(Set<Player> players) {
        Team team = findByPlayers(players);
        if (team == null) {
            team = new Team();
            team.setPlayers(players);
            team.setName(TeamUtil.getTeamName(team));
            team = saveOrUpdate(team);
        }
        return team;
    }

    @Override
    protected Set<String> getIndexedProperties() {
        return new HashSet<>(Arrays.asList("name", ALIAS_PREFIX + "players.lastName", ALIAS_PREFIX + "players.firstName"));
    }
}
