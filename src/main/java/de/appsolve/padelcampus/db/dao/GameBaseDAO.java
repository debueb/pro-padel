package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.*;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

;

/**
 * @author dominik
 */
@Component
public class GameBaseDAO extends BaseEntityDAO<Game> implements GameBaseDAOI {

    @Override
    public List<Game> findAllYoungerThanForGenderWithPlayers(LocalDate date, Gender gender, Customer customer) {

        Criteria criteria = getCriteria();
        criteria.setFetchMode("participants.players", FetchMode.JOIN);
        criteria.createAlias("event", "e");
        criteria.add(Restrictions.eq("customer", customer));
        criteria.add(Restrictions.gt("e.endDate", date));
        criteria.add(Restrictions.isNotEmpty("gameSets"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Game> games = criteria.list();
        Iterator<Game> iterator = games.iterator();

        while (iterator.hasNext()) {
            Game game = iterator.next();
            for (Participant participant : game.getParticipants()) {
                if (participant instanceof Team) {
                    Team t = (Team) participant;
                    Gender teamGender = null;
                    for (Player player : t.getPlayers()) {
                        if (teamGender == null) {
                            teamGender = player.getGender();
                        } else if (!teamGender.equals(player.getGender())) {
                            teamGender = Gender.mixed;
                        }
                    }
                    //check if game has been removed before to avoid IllegalStateException
                    if (games.contains(game) && !gender.equals(Gender.unisex) && (teamGender == null || !teamGender.equals(gender))) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        return games;
    }
}