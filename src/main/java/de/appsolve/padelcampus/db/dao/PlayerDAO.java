package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.MatchOffer;
import de.appsolve.padelcampus.db.model.Player;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.jadira.usertype.spi.utils.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class PlayerDAO extends SortedGenericDAO<Player> implements PlayerDAOI{

    private static final Logger log = Logger.getLogger(PlayerDAO.class);
    
    @Override
    public Player findByEmail(String email) {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("email", email);
        List<Player> players = findByAttributes(attributeMap);
        if (players.isEmpty()){
            return null;
        }
        if (players.size() == 1){
            return players.get(0);
        }
        log.warn("Found unexpected number of players for email ["+email+"]: "+players.size());
        return null;
    }

    @Override
    public Player findByUUID(String UUID) {
        return findByAttribute("UUID", UUID);
    }

    @Override
    public Player findByPasswordResetUUID(String UUID) {
        return findByAttribute("passwordResetUUID", UUID);
    }
    
    @Override
    public Player createOrUpdate(Player player){
        if (StringUtils.isEmpty(player.getUUID())){
            UUID randomUUID = UUID.randomUUID();
            player.setUUID(randomUUID.toString());
        }
        return saveOrUpdate(player);
    }

    @Override
    public List<Player> findRegistered() {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        criteria.add(Restrictions.isNotNull("passwordHash"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Player>) criteria.list();
    }

    @Override
    public List<Player> findPlayersInterestedIn(MatchOffer offer) {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(getGenericSuperClass(GenericDAO.class));
        criteria.add(Restrictions.isNotNull("enableMatchNotifications"));
        criteria.add(Restrictions.eq("enableMatchNotifications", true));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List<Player> interestedPlayers = (List<Player>) criteria.list();
        Iterator<Player> iterator = interestedPlayers.iterator();
        //remove all players that are not interested in the skill levels associated with the match offer
        while (iterator.hasNext()){
            Player player = iterator.next();
            if (Collections.disjoint(player.getNotificationSkillLevels(), offer.getSkillLevels())){
                iterator.remove();
            }
        }
        return interestedPlayers;
    }
}
