package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedBaseDAO;
import de.appsolve.padelcampus.db.model.MatchOffer;
import de.appsolve.padelcampus.db.model.Player;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class PlayerDAO extends SortedBaseDAO<Player> implements PlayerDAOI{

    @Override
    public Player findByEmail(String email) {
        return findByAttribute("email", email);
    }

    @Override
    public Player findByUUID(String UUID) {
        return findByAttribute("UUID", UUID);
    }
    
    @Override
    public Player findByUUIDWithDaySchedules(String UUID) {
        return findByUUIDFetchEagerly(UUID, "daySchedules");
    }

    @Override
    public Player findByPasswordResetUUID(String UUID) {
        return findByAttribute("passwordResetUUID", UUID);
    }
    
    @Override
    public Player saveOrUpdate(Player player){
        if (StringUtils.isEmpty(player.getUUID())){
            UUID randomUUID = UUID.randomUUID();
            player.setUUID(randomUUID.toString());
        }
        if (!StringUtils.isEmpty(player.getPassword())){
            player.setPasswordHash(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
            player.setSalted(true);
        }
        if (!StringUtils.isEmpty(player.getFirstName())){
            player.setFirstName(WordUtils.capitalizeFully(player.getFirstName().trim(), new char[]{' ', '-'}));
        }
        if (!StringUtils.isEmpty(player.getLastName())){
            player.setLastName(WordUtils.capitalizeFully(player.getLastName().trim(), new char[]{' ', '-'}));
        }
        if (!StringUtils.isEmpty(player.getEmail())){
            player.setEmail(player.getEmail().trim());
        }
        player.setCustomer(getCustomer());
        return super.saveOrUpdate(player);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Player> findRegistered() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("passwordHash"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Player>) criteria.list();
    }

    @Override
    public List<Player> findPlayersInterestedIn(MatchOffer offer) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("enableMatchNotifications"));
        criteria.add(Restrictions.eq("enableMatchNotifications", true));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
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
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Player> findPlayersRegisteredForEmails() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("passwordHash"));
        criteria.add(Restrictions.isNotNull("allowEmailContact"));
        criteria.add(Restrictions.eq("allowEmailContact", true));
        criteria.add(Restrictions.eq("customer", getCustomer()));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Player>) criteria.list();
    }
    
    @Override
    protected Set<String> getIndexedProperties(){
       return new HashSet<>(Arrays.asList("firstName", "lastName", "email", "phone")); 
    }
}
